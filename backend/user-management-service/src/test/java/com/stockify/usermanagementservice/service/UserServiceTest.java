package com.stockify.usermanagementservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockify.usermanagementservice.dto.*;
import com.stockify.usermanagementservice.model.BusinessUser;
import com.stockify.usermanagementservice.model.Role;
import com.stockify.usermanagementservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private userService userService;

    private MockWebServer mockWebServer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        mockWebServer = new MockWebServer();
        mockWebServer.start(8080);

        userService = new userService(userRepository, WebClient.builder().build());
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void stop() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void addUserValid() throws JsonProcessingException {
        ApiCallResponse apiCallResponse = new ApiCallResponse(200, "true");
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        apiCallResponse = new ApiCallResponse(200, "1");
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        apiCallResponse = new ApiCallResponse(200, "User successfully added to business");
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        UserRequest userRequest = new UserRequest("john", "smith", Role.EMPLOYEE, 111112, "john@mail.com");
        boolean result = userService.addUser(userRequest);

        assertTrue(result);
        verify(userRepository, times(1)).save(any(BusinessUser.class));
    }

    @Test
    public void addUserUserDoesntExist() throws JsonProcessingException {
        ApiCallResponse apiCallResponse = new ApiCallResponse(404, "false");
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        UserRequest userRequest = new UserRequest("john", "smith", Role.EMPLOYEE, 111112, "john@mail.com");
        boolean result = userService.addUser(userRequest);

        assertFalse(result);
        verify(userRepository, times(0)).save(any(BusinessUser.class));
    }

    @Test
    public void addUserUserDoesntExistNull() throws JsonProcessingException {
        ApiCallResponse apiCallResponse = new ApiCallResponse(404, null);
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        UserRequest userRequest = new UserRequest("john", "smith", Role.EMPLOYEE, 111112, "john@mail.com");
        boolean result = userService.addUser(userRequest);

        assertFalse(result);
        verify(userRepository, times(0)).save(any(BusinessUser.class));
    }

    @Test
    public void deleteUserVerify() throws JsonProcessingException {
        UserDetailsDto userDetailsDto = new UserDetailsDto(1, "john", "smith", "john@mail.com");
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(userDetailsDto))
        );

        ApiCallResponse apiCallResponse = new ApiCallResponse(200, "User successfully added to business");
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        deleteRequest deleteRequest = new deleteRequest(1);
        userService.deleteUser(deleteRequest);

        verify(userRepository, times(1)).deleteById(anyInt());
    }


    @Test
    public void updateUserValid() {
        BusinessUser businessUser = new BusinessUser(1, Role.EMPLOYEE, 1);
        when(userRepository.getReferenceById(anyInt())).thenReturn(businessUser);

        boolean result = userService.updateUser(new UpdateRequest(1, Role.MANAGER));
        verify(userRepository, times(1)).save(any());

        assertTrue(result, "Didn't perform save");
    }

    @Test
    public void updateUserNullRole() {
        boolean result = userService.updateUser(new UpdateRequest(1, null));
        verify(userRepository, times(0)).save(any());
        assertFalse(result, "Failed to catch null role");
    }

    @Test
    public void updateUserCatchEntityNotFoundException() {
        when(userRepository.getReferenceById(1)).thenThrow(EntityNotFoundException.class);

        boolean result = userService.updateUser(new UpdateRequest(1, Role.MANAGER));
        assertFalse(result, "Failed to catch EntityNotFoundException");
    }

    @Test
    public void getBusinessUsersValid() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("111111")
        );

        List<BusinessUser> businessUsers = new ArrayList<>();
        businessUsers.add(new BusinessUser(1, Role.OWNER, 111111));
        businessUsers.add(new BusinessUser(2, Role.EMPLOYEE, 111111));
        when(userRepository.findByBusinessCode(111111)).thenReturn(businessUsers);

        List<UserDetailsDto> users = new ArrayList<>();
        users.add(new UserDetailsDto(1, "john", "smith", "john@mail.com"));
        users.add(new UserDetailsDto(2, "jim", "cable", "jim@mail.com"));

        UserIdResponse userIdResponse = new UserIdResponse(200, users);
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(userIdResponse))
        );

        List<BusinessUserDto> result = userService.getBusinessUsers("john@mail.com");

        assertEquals(2, result.size());
        assertEquals(111111, result.get(0).getBusinessCode());
        assertEquals("john@mail.com", result.get(0).getEmail());
        assertEquals(111111, result.get(1).getBusinessCode());
        assertEquals("jim@mail.com", result.get(1).getEmail());
        verify(userRepository, times(1)).findByBusinessCode(111111);
    }



}
