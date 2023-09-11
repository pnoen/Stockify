package com.stockify.usermanagementservice.service;

import com.stockify.usermanagementservice.dto.*;
import com.stockify.usermanagementservice.model.BusinessUser;
import com.stockify.usermanagementservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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

        // TODO: Get the users name from the user service/table with user id
        List<BusinessUserDto> userDtos = users.stream().map(user -> BusinessUserDto.builder() // currently without the names
                .id(user.getId())
//                .role_id(user.getRole_id())
                .companyId(user.getCompanyId())
                .role(user.getRole())
                .build()
        ).toList();
        return userDtos;
    }
}
