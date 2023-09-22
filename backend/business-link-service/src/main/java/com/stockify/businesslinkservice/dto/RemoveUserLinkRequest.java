package com.stockify.businesslinkservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class RemoveUserLinkRequest {
    @NonNull
    private Integer businessCode;

    @NonNull
    private Integer userId;
}
