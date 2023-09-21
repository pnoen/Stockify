package com.stockify.usermanagementservice.controller;

import com.stockify.usermanagementservice.dto.RoleDto;
import com.stockify.usermanagementservice.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    @GetMapping("/getRoles")
    public List<RoleDto> getRoles() {
        // Create a list to hold RoleDto objects
        List<RoleDto> roleDtoList = new ArrayList<>();

        // Iterate through the Role enum values and convert them to RoleDto objects
        for (Role role : Role.values()) {
            RoleDto roleDto = new RoleDto(role.getId(), role.getLabel());
            roleDtoList.add(roleDto);
        }

        return roleDtoList;
    }

}
