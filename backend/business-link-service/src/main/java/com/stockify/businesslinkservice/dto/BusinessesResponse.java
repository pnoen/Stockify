package com.stockify.businesslinkservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessesResponse {
    private List<BusinessDto> businesses;
    private int statusCode;

}
