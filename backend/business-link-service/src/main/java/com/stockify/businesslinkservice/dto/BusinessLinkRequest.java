package com.stockify.businesslinkservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class BusinessLinkRequest {
    @NonNull
    private Integer businessCode;

    @NonNull
    private String email;
}
