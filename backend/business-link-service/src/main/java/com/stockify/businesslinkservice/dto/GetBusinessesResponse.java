package com.stockify.businesslinkservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetBusinessesResponse {
    private List<BusinessDto> businesses;
    private String message;
}
