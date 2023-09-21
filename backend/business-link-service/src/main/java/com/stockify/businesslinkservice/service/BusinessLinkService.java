package com.stockify.businesslinkservice.service;

import com.stockify.businesslinkservice.dto.*;
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
                .map(code -> String.valueOf(code))
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

    public List<BusinessDto> getBusinesses(int customerId) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByCustomerId(customerId);

        List<Integer> businessCodes = businessLinks.stream()
                .map(businessLink -> businessLink.getBusinessCode())
                .toList();

        UriComponentsBuilder uriBuilder = fromHttpUrl("http://localhost:8080/account/getBusinesses");
        uriBuilder.queryParam("businessCodes", businessCodes.stream()
                .map(id -> String.valueOf(id))
                .collect(Collectors.joining(","))
        );
        URI uri = uriBuilder.build().encode().toUri();

        BusinessesResponse responseEntity = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(BusinessesResponse.class)
                .block();

        return responseEntity.getBusinesses();
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
