package com.example.DAuction.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PurchasedResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal boughtPrice;
}
