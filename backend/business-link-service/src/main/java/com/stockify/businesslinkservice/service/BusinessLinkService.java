package com.stockify.businesslinkservice.service;

import com.stockify.businesslinkservice.dto.CustomerDto;
import com.stockify.businesslinkservice.dto.LinkRequest;
import com.stockify.businesslinkservice.dto.UserIdsResponse;
import com.stockify.businesslinkservice.model.BusinessLink;
import com.stockify.businesslinkservice.repository.BusinessLinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;


@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessLinkService {

    private final BusinessLinkRepository businessLinkRepository;
    private final WebClient webClient;

    public String createLink(LinkRequest linkRequest) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndCustomerId(
                linkRequest.getBusinessCode(),
                linkRequest.getCustomerId()
        );

        if (!businessLinks.isEmpty()) {
            return "Link already exists between business and customer.";
        }

        BusinessLink businessLink = BusinessLink.builder()
                .businessCode(linkRequest.getBusinessCode())
                .customerId(linkRequest.getCustomerId())
                .build();
        businessLinkRepository.save(businessLink);

        return null;
    }

    public List<CustomerDto> getCustomers(int businessCode) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCode(businessCode);

        List<Integer> customerIds =  businessLinks.stream()
                .map(businessLink -> businessLink.getCustomerId())
                .toList();

        UriComponentsBuilder uriBuilder = fromHttpUrl("http://localhost:8080/account/getUsers");
        uriBuilder.queryParam("userIds", customerIds.stream()
                .map(id -> String.valueOf(id))
                .collect(Collectors.joining(","))
        );
        URI uri = uriBuilder.build().encode().toUri();

        UserIdsResponse responseEntity = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserIdsResponse.class)
                .block();

        return responseEntity.getUsers();
    }

    public String removeLink(LinkRequest linkRequest) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndCustomerId(
                linkRequest.getBusinessCode(),
                linkRequest.getCustomerId()
        );

        if (businessLinks.isEmpty()) {
            return "Link doesn't exist.";
        }

        businessLinkRepository.deleteByBusinessCodeAndCustomerId(
                linkRequest.getBusinessCode(),
                linkRequest.getCustomerId()
        );
        return null;
    }
}
