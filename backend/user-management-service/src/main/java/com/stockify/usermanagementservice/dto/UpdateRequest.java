package com.stockify.usermanagementservice.dto;

import com.stockify.usermanagementservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateRequest {
    private int id;
    private Role role;
}
