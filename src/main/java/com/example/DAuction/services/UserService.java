package com.example.DAuction.services;

import com.example.DAuction.dto.request.BidRequest;
import com.example.DAuction.dto.request.ItemRequest;
import com.example.DAuction.dto.request.PurchasedRequest;
import com.example.DAuction.dto.request.UserRequest;
import com.example.DAuction.dto.response.BidResponse;
import com.example.DAuction.dto.response.ItemResponse;
import com.example.DAuction.dto.response.PurchasedResponse;
import com.example.DAuction.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse register(UserRequest userRequest);
    ItemResponse uploadItem(ItemRequest itemRequest);
    ItemResponse editItem(ItemRequest itemRequest);
    void deleteAuctionedItem(ItemRequest itemRequest);
    List<ItemResponse> viewAuctionedItems();
    ItemResponse findItemById(ItemRequest request);
    List<PurchasedResponse> purchasedItems(PurchasedRequest purchasedRequest);
    BidResponse bidOnItem(BidRequest bidRequest);
    BidResponse updateBid(BidRequest bidRequest);
}
