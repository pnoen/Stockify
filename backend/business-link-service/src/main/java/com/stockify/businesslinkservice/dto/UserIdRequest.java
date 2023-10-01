package com.stockify.businesslinkservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserIdRequest {
    private String firstName;
    private String lastName;
    private String email;
}
