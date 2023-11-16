package com.example.DAuction.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @OneToOne
    private Location location;
    private String phoneNumber;
    private String email;
    @OneToMany
    private List<Item> auctionedItem;
    @OneToMany
    private List<Item> boughtItems = new ArrayList<>();
    private Status status;
}
