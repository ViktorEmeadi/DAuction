package com.example.DAuction.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateUploaded;
    private LocalDateTime closingDate;
    private BigDecimal startPrice;
}
