package com.example.DAuction.dto.request;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class BidRequest {
    private Long itemId;
    private BigDecimal price;
    private Long userId;
}
