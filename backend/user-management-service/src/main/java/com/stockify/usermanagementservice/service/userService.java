package com.stockify.usermanagementservice.service;

import com.stockify.usermanagementservice.dto.UserRequest;
import com.stockify.usermanagementservice.dto.deleteRequest;
import com.stockify.usermanagementservice.model.BusinessUser;
import com.stockify.usermanagementservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
                    .role_id(userRequest.getRole_id())
                    .company_id(userRequest.getCompany_id())
                    .build();
            userRepository.save(user);
            log.info("BusinessUser {} is saved", user.getCompany_id());
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
}
