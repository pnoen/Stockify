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
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndUserId(
                linkRequest.getBusinessCode(),
                linkRequest.getUserId()
        );

        if (!businessLinks.isEmpty()) {
            return "Link already exists between business and user.";
        }

        BusinessLink businessLink = BusinessLink.builder()
                .businessCode(linkRequest.getBusinessCode())
                .userId(linkRequest.getUserId())
                .build();
        businessLinkRepository.save(businessLink);

        return null;
    }

    public List<UserDto> getUsers(String email) {
        UriComponentsBuilder uriBuilder = fromHttpUrl("http://localhost:8080/account/getBusinessCode");
        uriBuilder.queryParam("email", email);
        URI uri = uriBuilder.build().encode().toUri();

        int businessCode = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCode(businessCode);

        List<Integer> userIds =  businessLinks.stream()
                .map(businessLink -> businessLink.getUserId())
                .toList();

        uriBuilder = fromHttpUrl("http://localhost:8080/account/getUsers");
        uriBuilder.queryParam("userIds", userIds.stream()
                .map(code -> String.valueOf(code))
                .collect(Collectors.joining(","))
        );
        uri = uriBuilder.build().encode().toUri();

        UserIdsResponse responseEntity = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserIdsResponse.class)
                .block();

        return responseEntity.getUsers();
    }

    public List<BusinessDto> getBusinesses(int userId) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByUserId(userId);

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
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndUserId(
                linkRequest.getBusinessCode(),
                linkRequest.getUserId()
        );

        if (businessLinks.isEmpty()) {
            return "Link doesn't exist.";
        }

        businessLinkRepository.deleteByBusinessCodeAndUserId(
                linkRequest.getBusinessCode(),
                linkRequest.getUserId()
        );
        return null;
    }
}
