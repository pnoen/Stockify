package com.stockify.usermanagementservice.service;

import com.stockify.usermanagementservice.dto.*;
import com.stockify.usermanagementservice.model.BusinessUser;
import com.stockify.usermanagementservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        UserCheckRequest request = new UserCheckRequest(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail());
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

            UpdateUserBusinessCodeRequest updateRequest = new UpdateUserBusinessCodeRequest(userRequest.getEmail(), userRequest.getBusinessCode());
            ResponseEntity<ApiCallResponse> webRequest = webClient.post()
                    .uri("http://localhost:8080/account/updateUserBusinessCode")
                    .bodyValue(updateRequest)
                    .retrieve()
                    .toEntity(ApiCallResponse.class)
                    .block();

            System.out.println(webRequest.getBody().getMessage());
            BusinessUser user = BusinessUser.builder()
                    .id(id)
//                    .role_id(userRequest.getRole_id())
                    .businessCode(userRequest.getBusinessCode())
                    .role(userRequest.getRole())
                    .build();
            userRepository.save(user);
            log.info("BusinessUser {} is saved", user.getBusinessCode());
            return true;
        }
        else{
            //throw new IllegalArgumentException("User is not found");
            return false;
        }


    }

    public void deleteUser(deleteRequest deleteRequest) {
        userRepository.deleteById(deleteRequest.getId());

        UriComponentsBuilder uriBuilder = fromHttpUrl("http://localhost:8080/account/getUserDetails");
        uriBuilder.queryParam("userId", deleteRequest.getId());
        URI uri = uriBuilder.build().encode().toUri();

        UserDetailsDto userDetails = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserDetailsDto.class)
                .block();

        UpdateUserBusinessCodeRequest updateRequest = new UpdateUserBusinessCodeRequest(userDetails.getEmail(),0);
        ResponseEntity<ApiCallResponse> webRequest = webClient.post()
                .uri("http://localhost:8080/account/updateUserBusinessCode")
                .bodyValue(updateRequest)
                .retrieve()
                .toEntity(ApiCallResponse.class)
                .block();

        System.out.println(webRequest.getBody().getMessage());
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


    public List<BusinessUserDto> getBusinessUsers(String email) {

        UriComponentsBuilder uriBuilder = fromHttpUrl("http://localhost:8080/account/getBusinessCode");
        uriBuilder.queryParam("email", email);
        URI uri = uriBuilder.build().encode().toUri();

        int businessCode = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        List<BusinessUser> users = userRepository.findByBusinessCode(businessCode);

        List<Integer> userIds = users.stream()
                .map(user -> user.getId())
                .toList();


        uriBuilder = fromHttpUrl("http://localhost:8080/account/getUsers");
        uriBuilder.queryParam("userIds", userIds.stream()
                .map(id -> String.valueOf(id))
                .collect(Collectors.joining(","))
        );
        uri = uriBuilder.build().encode().toUri();

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
                    .businessCode(user.getBusinessCode())
                    .role(user.getRole())
                    .firstName(userDetails.getFirstName())
                    .lastName(userDetails.getLastName())
                    .email(userDetails.getEmail())
                    .build();
        }).toList();
    }
}
