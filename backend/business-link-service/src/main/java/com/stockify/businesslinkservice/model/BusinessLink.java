package com.stockify.businesslinkservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BusinessLink {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private int businessCode;
    private int userId;
}
