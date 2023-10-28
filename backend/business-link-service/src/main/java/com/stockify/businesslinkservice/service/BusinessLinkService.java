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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;


@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessLinkService {

    private final BusinessLinkRepository businessLinkRepository;
    private final WebClient webClient;

    public String createBusinessLink(BusinessLinkRequest businessLinkRequest) {
        UriComponentsBuilder uriBuilder = fromHttpUrl("http://localhost:8080/account/getUserIdByEmail");
        uriBuilder.queryParam("email", businessLinkRequest.getEmail());
        URI uri = uriBuilder.build().encode().toUri();

        int userId = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        if (userId == -1) {
            return "Unable to find user.";
        }

        List<Integer> businessCodes = new ArrayList<>();
        businessCodes.add(businessLinkRequest.getBusinessCode());
        uriBuilder = fromHttpUrl("http://localhost:8080/account/getBusinesses");
        uriBuilder.queryParam("businessCodes",
                businessCodes
        );
        uri = uriBuilder.build().encode().toUri();

        BusinessesResponse responseEntity = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(BusinessesResponse.class)
                .block();

        if (responseEntity.getBusinesses().isEmpty()) {
            return "Unable to find business.";
        }

        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndUserId(
                businessLinkRequest.getBusinessCode(),
                userId
        );

        if (!businessLinks.isEmpty()) {
            return "Link already exists between business and user.";
        }

        BusinessLink businessLink = BusinessLink.builder()
                .businessCode(businessLinkRequest.getBusinessCode())
                .userId(userId)
                .build();
        businessLinkRepository.save(businessLink);

        return null;
    }

    public String createUserLink(UserLinkRequest userLinkRequest) {
        UserIdRequest userIdRequest = UserIdRequest.builder()
                .firstName(userLinkRequest.getFirstName())
                .lastName(userLinkRequest.getLastName())
                .email(userLinkRequest.getEmail())
                .build();

        ApiCallResponse responseEntity = webClient.post()
                .uri("http://localhost:8080/account/getUserId")
                .bodyValue(userIdRequest)
                .retrieve()
                .bodyToMono(ApiCallResponse.class)
                .block();

        int userId = Integer.parseInt(responseEntity.getMessage());
        if (userId == -1) {
            return "Unable to find user.";
        }

        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndUserId(
                userLinkRequest.getBusinessCode(),
                userId
        );

        if (!businessLinks.isEmpty()) {
            return "Link already exists between business and user.";
        }

        BusinessLink businessLink = BusinessLink.builder()
                .businessCode(userLinkRequest.getBusinessCode())
                .userId(userId)
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

        List<Integer> userIds = businessLinks.stream()
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

    public List<BusinessDto> getBusinesses(String email) {
        UriComponentsBuilder uriBuilder = fromHttpUrl("http://localhost:8080/account/getUserIdByEmail");
        uriBuilder.queryParam("email", email);
        URI uri = uriBuilder.build().encode().toUri();

        int userId = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        List<BusinessLink> businessLinks = businessLinkRepository.findByUserId(userId);

        List<Integer> businessCodes = businessLinks.stream()
                .map(businessLink -> businessLink.getBusinessCode())
                .toList();

        uriBuilder = fromHttpUrl("http://localhost:8080/account/getBusinesses");
        uriBuilder.queryParam("businessCodes", businessCodes.stream()
                .map(id -> String.valueOf(id))
                .collect(Collectors.joining(","))
        );
        uri = uriBuilder.build().encode().toUri();

        BusinessesResponse responseEntity = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(BusinessesResponse.class)
                .block();

        return responseEntity.getBusinesses();
    }

    public String removeUserLink(RemoveUserLinkRequest removeUserLinkRequest) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndUserId(
                removeUserLinkRequest.getBusinessCode(),
                removeUserLinkRequest.getUserId()
        );

        if (businessLinks.isEmpty()) {
            return "Link doesn't exist.";
        }

        businessLinkRepository.deleteByBusinessCodeAndUserId(
                removeUserLinkRequest.getBusinessCode(),
                removeUserLinkRequest.getUserId()
        );
        return null;
    }

    public String removeBusinessLink(BusinessLinkRequest removeBusinessLinkRequest) {
        UriComponentsBuilder uriBuilder = fromHttpUrl("http://localhost:8080/account/getUserIdByEmail");
        uriBuilder.queryParam("email", removeBusinessLinkRequest.getEmail());
        URI uri = uriBuilder.build().encode().toUri();

        int userId = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        if (userId == -1) {
            return "Unable to find user.";
        }

        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndUserId(
                removeBusinessLinkRequest.getBusinessCode(),
                userId
        );

        if (businessLinks.isEmpty()) {
            return "Link doesn't exist.";
        }

        businessLinkRepository.deleteByBusinessCodeAndUserId(
                removeBusinessLinkRequest.getBusinessCode(),
                userId
        );
        return null;
    }
}
