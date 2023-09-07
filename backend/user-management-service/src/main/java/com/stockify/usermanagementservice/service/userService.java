package com.stockify.usermanagementservice.service;

import com.stockify.usermanagementservice.dto.BusinessUserDto;
import com.stockify.usermanagementservice.dto.UpdateRequest;
import com.stockify.usermanagementservice.dto.UserRequest;
import com.stockify.usermanagementservice.dto.deleteRequest;
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

        Boolean result = true;
//        Boolean result = webClient.get()
//                .uri("http://localhost:8082/api/......")
//                .retrieve()
//                .bodyToMono(Boolean.class)
//                .block();

        if(result) {
//            int id = webClient.get()
//                    .uri("http://localhost:8082/api/...")
//                    .retrieve()
//                    .bodyToMono(Integer.class)
//                    .block();

            BusinessUser user = BusinessUser.builder()
                    //.id(id)
//                    .role_id(userRequest.getRole_id())
                    .company_id(userRequest.getCompany_id())
                    .role(userRequest.getRole())
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


    public List<BusinessUserDto> getAllUsers() {
        // TODO: Find users in a company instead of all
        List<BusinessUser> users = userRepository.findAll();

        // TODO: Get the users name from the user service/table with user id
        List<BusinessUserDto> userDtos = users.stream().map(user -> BusinessUserDto.builder() // currently without the names
                .id(user.getId())
//                .role_id(user.getRole_id())
                .company_id(user.getCompany_id())
                .role(user.getRole())
                .build()
        ).toList();
        return userDtos;
    }
}
