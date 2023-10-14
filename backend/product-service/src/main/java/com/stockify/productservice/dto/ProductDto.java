package com.stockify.productservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private int id;
    private String name;
    private String description;
    private int quantity;
    private float price;
    private int businessCode;
    private String businessName;
    private String imageURL;
}
