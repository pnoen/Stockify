package com.stockify.usermanagementservice.dto;

import com.stockify.usermanagementservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessUserDto {
    private int id;
//    private int role_id;
    private int company_id;
    private Role role;
}
