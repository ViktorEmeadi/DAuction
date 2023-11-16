package com.example.DAuction.data.repositories;

import com.example.DAuction.data.model.Bid;
import com.example.DAuction.data.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Optional<Bid> findByUserId(Long userId);

    List<Bid> findByItemOrderByPriceDesc(Item item);
}
