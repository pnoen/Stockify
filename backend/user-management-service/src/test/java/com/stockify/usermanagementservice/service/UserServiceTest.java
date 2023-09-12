package com.stockify.usermanagementservice.service;

import com.stockify.usermanagementservice.dto.UpdateRequest;
import com.stockify.usermanagementservice.model.BusinessUser;
import com.stockify.usermanagementservice.model.Role;
import com.stockify.usermanagementservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private userService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
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


}
