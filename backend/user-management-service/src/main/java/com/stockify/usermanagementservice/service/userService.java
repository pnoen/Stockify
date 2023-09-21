package com.stockify.usermanagementservice.service;

import com.stockify.usermanagementservice.dto.*;
import com.stockify.usermanagementservice.model.BusinessUser;
import com.stockify.usermanagementservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class userService {

    private final UserRepository userRepository;
    private final WebClient webClient;
    public boolean addUser(UserRequest userRequest) {
        UserCheckRequest request = new UserCheckRequest(userRequest.getFirst_name(), userRequest.getLast_name(), userRequest.getEmail());
        Boolean result = false;

        ResponseEntity<ApiCallResponse> responseEntity = webClient.post()
                .uri("http://localhost:8080/account/checkUserExist")
                .bodyValue(request)
                .retrieve()
                .toEntity(ApiCallResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            ApiCallResponse response = responseEntity.getBody();

            if (response != null) {
                String message = response.getMessage();
                 result = Boolean.parseBoolean(message);
            }
        }

        if(result) {
            int id = -1;
            responseEntity = webClient.post()
                    .uri("http://localhost:8080/account/getUserId")
                    .bodyValue(request)
                    .retrieve()
                    .toEntity(ApiCallResponse.class)
                    .block();

            if (responseEntity != null) {
                ApiCallResponse apiResponse = responseEntity.getBody();

                if (apiResponse != null) {
                    id = Integer.parseInt(apiResponse.getMessage());
                }
            }

            BusinessUser user = BusinessUser.builder()
                    .id(id)
//                    .role_id(userRequest.getRole_id())
                    .companyId(userRequest.getCompanyId())
                    .role(userRequest.getRole())
                    .build();
            userRepository.save(user);
            log.info("BusinessUser {} is saved", user.getCompanyId());
            return true;
        }
        else{
            //throw new IllegalArgumentException("User is not found");
            return false;
        }


    }

    public void deleteUser(deleteRequest deleteRequest) {
        userRepository.deleteById(deleteRequest.getId());
    }

    public boolean updateUser(UpdateRequest updateRequest) {
        if (updateRequest.getRole() == null) {
            return false;
        }

        try {
            BusinessUser user = userRepository.getReferenceById(updateRequest.getId());
            user.setRole(updateRequest.getRole());
            userRepository.save(user);
        }
        catch (EntityNotFoundException e) {
            return false;
        }
        return true;
    }


    public List<BusinessUserDto> getBusinessUsers(GetBusinessUsersRequest getBusinessUsersRequest) {
        List<BusinessUser> users = userRepository.findByCompanyId(getBusinessUsersRequest.getCompanyId());

        List<Integer> userIds = users.stream()
                .map(user -> user.getId())
                .toList();

        UriComponentsBuilder uriBuilder = fromHttpUrl("http://localhost:8080/account/getUsers");
        uriBuilder.queryParam("userIds", userIds.stream()
                .map(id -> String.valueOf(id))
                .collect(Collectors.joining(","))
        );
        URI uri = uriBuilder.build().encode().toUri();

        UserIdResponse responseEntity = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserIdResponse.class)
                .block();

        List<UserDetailsDto> result = responseEntity.getUsers();
        return users.stream().map(user -> {
            UserDetailsDto userDetails = result.stream()
                    .filter(res -> res.getId() == user.getId())
                    .toList()
                    .get(0);

            return BusinessUserDto.builder()
                    .id(user.getId())
                    .companyId(user.getCompanyId())
                    .role(user.getRole())
                    .firstName(userDetails.getFirstName())
                    .lastName(userDetails.getLastName())
                    .email(userDetails.getEmail())
                    .build();
        }).toList();
    }
}
