package com.stockify.businesslinkservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class RemoveLinkRequest {
    @NonNull
    private Integer businessCode;

    @NonNull
    private Integer userId;
}
