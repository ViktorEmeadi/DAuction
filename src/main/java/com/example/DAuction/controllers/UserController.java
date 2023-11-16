package com.example.DAuction.controllers;

import com.example.DAuction.data.model.User;
import com.example.DAuction.dto.request.BidRequest;
import com.example.DAuction.dto.request.ItemRequest;
import com.example.DAuction.dto.request.PurchasedRequest;
import com.example.DAuction.dto.request.UserRequest;
import com.example.DAuction.dto.response.BidResponse;
import com.example.DAuction.dto.response.ItemResponse;
import com.example.DAuction.dto.response.PurchasedResponse;
import com.example.DAuction.dto.response.UserResponse;
import com.example.DAuction.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/dauction")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest){
        UserResponse user = userService.register(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    @PostMapping("upload")
    public ResponseEntity<?> uploadItem(@RequestBody ItemRequest itemRequest){
        ItemResponse item = userService.uploadItem(itemRequest);
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }
    @GetMapping("edit")
    public ResponseEntity<?> editItem(@RequestBody ItemRequest itemRequest){
        ItemResponse response = userService.editItem(itemRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("delete")
    public ResponseEntity<?> deleteItem(@RequestBody ItemRequest itemRequest){
        userService.deleteAuctionedItem(itemRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Delete successful");
    }
    @GetMapping("view-all")
    public ResponseEntity<?> viewAll(){
        List<ItemResponse> items = userService.viewAuctionedItems();
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }
    @GetMapping("find")
    public ResponseEntity<?> findById(@RequestBody ItemRequest itemRequest){
        ItemResponse item = userService.findItemById(itemRequest);
        return ResponseEntity.status(HttpStatus.FOUND).body(item);
    }
    @PostMapping("bid")
    public ResponseEntity<?> bidOnItem(@RequestBody BidRequest bidRequest){
        BidResponse response = userService.bidOnItem(bidRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("update-bid")
    public ResponseEntity<?> editBid(@RequestBody BidRequest bidRequest){
        BidResponse response = userService.updateBid(bidRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
    @GetMapping("purchased-items")
    public ResponseEntity<?> purchasedItems(@RequestBody PurchasedRequest purchasedRequest){
        List<PurchasedResponse> purchasedItems = userService.purchasedItems(purchasedRequest);
        return ResponseEntity.status(HttpStatus.FOUND).body(purchasedItems);
    }
}
