package com.stockify.usermanagementservice.controller;

import com.stockify.usermanagementservice.dto.RoleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getRolesVerify() {
        List<RoleDto> result = roleController.getRoles();

        assertEquals(3, result.size());
        assertEquals("Owner", result.get(0).getLabel());
        assertEquals("Manager", result.get(1).getLabel());
        assertEquals("Employee", result.get(2).getLabel());
    }

}