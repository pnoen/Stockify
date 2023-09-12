package com.stockify.usermanagementservice.controller;

import com.stockify.usermanagementservice.dto.BusinessUserDto;
import com.stockify.usermanagementservice.dto.GetBusinessUsersRequest;
import com.stockify.usermanagementservice.dto.UpdateRequest;
import com.stockify.usermanagementservice.model.Role;
import com.stockify.usermanagementservice.service.userService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void updateUserVerify() {
        when(userService.updateUser(any(UpdateRequest.class))).thenReturn(true);
        boolean result = userController.updateUser(new UpdateRequest(1, Role.EMPLOYEE));
        verify(userService, times(1)).updateUser(any(UpdateRequest.class));

        assertTrue(result, "Returned incorrect value");
    }

    @Test
    public void getBusinessUsersVerify() {
        List<BusinessUserDto> businessUserDtos = new ArrayList<>();
        businessUserDtos.add(new BusinessUserDto(1, 1, Role.OWNER, "john", "cable", "john@mail.com"));
        businessUserDtos.add(new BusinessUserDto(2, 1, Role.EMPLOYEE, "jim", "smith", "jim@mail.com"));

        when(userService.getBusinessUsers(any(GetBusinessUsersRequest.class))).thenReturn(businessUserDtos);
        List<BusinessUserDto> result = userController.getBusinessUsers(new GetBusinessUsersRequest(1));
        verify(userService, times(1)).getBusinessUsers(any(GetBusinessUsersRequest.class));

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("john");
        assertThat(result.get(1).getFirstName()).isEqualTo("jim");
    }
}
