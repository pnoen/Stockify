package com.stockify.usermanagementservice.controller;

import com.stockify.usermanagementservice.dto.BusinessUserDto;
import com.stockify.usermanagementservice.dto.UpdateRequest;
import com.stockify.usermanagementservice.dto.UserRequest;
import com.stockify.usermanagementservice.dto.deleteRequest;
import com.stockify.usermanagementservice.model.Role;
import com.stockify.usermanagementservice.service.userService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private userService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addUserVerify() {
        when(userService.addUser(any(UserRequest.class))).thenReturn(true);

        UserRequest userRequest = new UserRequest("john", "smith", Role.OWNER, 111111, "john@mail.com");
        boolean result = userController.addUser(userRequest);

        assertTrue(result, "Returned incorrect value");
        verify(userService, times(1)).addUser(any(UserRequest.class));
    }

    @Test
    public void deleteUserVerify() {
        deleteRequest deleteRequest = new deleteRequest(1);
        userController.deleteUser(deleteRequest);

        verify(userService, times(1)).deleteUser(any(deleteRequest.class));
    }

    @Test
    public void updateUserVerifyTrue() {
        when(userService.updateUser(any(UpdateRequest.class))).thenReturn(true);
        boolean result = userController.updateUser(new UpdateRequest(1, Role.EMPLOYEE));
        verify(userService, times(1)).updateUser(any(UpdateRequest.class));

        assertTrue(result, "Returned incorrect value");
    }

    @Test
    public void updateUserVerifyFalse() {
        when(userService.updateUser(any(UpdateRequest.class))).thenReturn(false);
        boolean result = userController.updateUser(new UpdateRequest(1, Role.EMPLOYEE));
        verify(userService, times(1)).updateUser(any(UpdateRequest.class));

        assertFalse(result, "Returned incorrect value");
    }

    @Test
    public void getBusinessUsersVerify() {
        List<BusinessUserDto> businessUserDtos = new ArrayList<>();
        businessUserDtos.add(new BusinessUserDto(1, 1, Role.OWNER, "john", "smith", "john@mail.com"));
        businessUserDtos.add(new BusinessUserDto(2, 1, Role.EMPLOYEE, "jim", "cable", "jim@mail.com"));
        when(userService.getBusinessUsers("john@mail.com")).thenReturn(businessUserDtos);

        List<BusinessUserDto> result = userController.getBusinessUsers("john@mail.com");

        assertEquals(2, result.size());
        assertEquals("john", result.get(0).getFirstName());
        assertEquals("jim", result.get(1).getFirstName());
        verify(userService, times(1)).getBusinessUsers("john@mail.com");
    }
}
