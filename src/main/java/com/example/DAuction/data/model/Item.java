package com.example.DAuction.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String name;
    private LocalDateTime dateUploaded;
    private LocalDateTime closingDate;
    private BigDecimal startPrice;
    private Long userId;
    private boolean auctioned;
    private boolean bought;
    private boolean expired;
    @OneToMany
    List<Bid> pricedUsers;
    private BigDecimal boughtPrice;
}
