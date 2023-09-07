package com.stockify.usermanagementservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetBusinessUsersRequest {
    private int companyId;
}
