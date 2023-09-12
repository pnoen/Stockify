package com.stockify.usermanagementservice.service;

import com.stockify.usermanagementservice.dto.*;
import com.stockify.usermanagementservice.model.BusinessUser;
import com.stockify.usermanagementservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
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

        Boolean result = webClient.post()
                .uri(String.format("http://localhost:8080/account/checkUserExist?firstName=%s&lastName=%s&email=%s", userRequest.getFirst_name(), userRequest.getLast_name(), userRequest.getEmail()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(result) {
            int id = webClient.post()
                    .uri(String.format("http://localhost:8080/account/getUserId?firstName=%s&lastName=%s&email=%s", userRequest.getFirst_name(), userRequest.getLast_name(), userRequest.getEmail()))
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();

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

        List<UserDetailsDto> result = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDetailsDto>>() {})
                .block();

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
