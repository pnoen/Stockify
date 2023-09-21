package com.stockify.businesslinkservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetCustomersResponse {
    private List<CustomerDto> customers;
    private String message;
}
