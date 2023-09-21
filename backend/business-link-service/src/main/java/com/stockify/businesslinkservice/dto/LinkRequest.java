package com.stockify.businesslinkservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class LinkRequest {
    @NonNull
    private Integer businessCode;

    @NonNull
    private Integer customerId;
}
