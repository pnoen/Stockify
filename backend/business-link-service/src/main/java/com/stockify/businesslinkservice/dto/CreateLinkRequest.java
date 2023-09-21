package com.stockify.businesslinkservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class CreateLinkRequest {
    @NonNull
    private Integer businessCode;

    @NonNull
    private Integer customerId;
}
