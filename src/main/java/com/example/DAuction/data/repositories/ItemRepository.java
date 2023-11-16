package com.example.DAuction.data.repositories;

import com.example.DAuction.data.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByUserId(Long userId);

    List<Item> findExpiredItems();
}
