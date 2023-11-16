package com.example.DAuction.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long userId;
    private String name;
    private String description;
    private LocalDateTime dateUploaded;
    private LocalDateTime closingDate;
    private BigDecimal startPrice;
    private Long id;
}
