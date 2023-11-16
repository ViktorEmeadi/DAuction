package com.example.DAuction.services;

import com.example.DAuction.data.model.Bid;
import com.example.DAuction.data.model.Item;
import com.example.DAuction.data.model.User;
import com.example.DAuction.data.repositories.BidRepository;
import com.example.DAuction.data.repositories.ItemRepository;
import com.example.DAuction.data.repositories.UserRepository;
import com.example.DAuction.dto.request.BidRequest;
import com.example.DAuction.dto.request.ItemRequest;
import com.example.DAuction.dto.request.PurchasedRequest;
import com.example.DAuction.dto.request.UserRequest;
import com.example.DAuction.dto.response.BidResponse;
import com.example.DAuction.dto.response.ItemResponse;
import com.example.DAuction.dto.response.PurchasedResponse;
import com.example.DAuction.dto.response.UserResponse;
import com.example.DAuction.exception.UserException;
import com.example.DAuction.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final Utils utils;
    private final BidRepository bidRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public UserResponse register(UserRequest userRequest) {
        UserResponse response = new UserResponse();
        Optional<User> user = userRepository.findUserByEmail(userRequest.getEmail());
        if (user.isPresent()){
            throw new UserException("Email already registered", 400);
        }
        User registeredUser = utils.register(userRequest);
        BeanUtils.copyProperties(registeredUser, response);
        return response;
    }

    @Override
    public ItemResponse uploadItem(ItemRequest itemRequest) {
        ItemResponse itemResponse = new ItemResponse();
        Optional<User> user = userRepository.findById(itemRequest.getUserId());
        if(user.isEmpty()){
            throw new UserException("User not found",400);
        }
        utils.validateDate(LocalDateTime.now(), itemRequest.getClosingDate());
        Item item = Item.builder()
                .name(itemResponse.getName())
                .description(itemResponse.getDescription())
                .startPrice(itemRequest.getStartPrice())
                .dateUploaded(LocalDateTime.now())
                .closingDate(itemRequest.getClosingDate())
                .userId(itemRequest.getUserId())
                .auctioned(true)
                .bought(false)
                .build();
        boolean expired = utils.isAuctionExpired(LocalDateTime.now(), itemRequest.getClosingDate());
        if (expired){
            item.setAuctioned(false);
            item.setExpired(true);
        }
        BeanUtils.copyProperties(item, itemResponse);
        return itemResponse;
    }
    @Override
    public ItemResponse editItem(ItemRequest itemRequest){
        ItemResponse itemResponse = new ItemResponse();
        Optional<Item> foundItem = itemRepository.findByUserId(itemRequest.getUserId());
        Optional<User> foundUser = userRepository.findById(itemRequest.getUserId());
        if (foundUser.isEmpty()){
            throw new UserException("User with id " + itemRequest.getUserId() + " does not exist", 400);
        }
        if (foundItem.isEmpty()){
            throw new UserException("You don't have any auctioned item", 400);
        }
        Item item = foundItem.get();
        item.setName(itemRequest.getName());
        item.setDescription(item.getDescription());
        item.setClosingDate(itemRequest.getClosingDate());
        item.setStartPrice(itemRequest.getStartPrice());
        BeanUtils.copyProperties(item, itemResponse);
        return itemResponse;
    }

    @Override
    public void deleteAuctionedItem(ItemRequest itemRequest) {
        Optional<User> user = userRepository.findById(itemRequest.getUserId());
        if (user.isEmpty()){
            throw new UserException("User not found", 400);
        }
        Optional<Item> foundItem = itemRepository.findById(itemRequest.getId());
        if (foundItem.isEmpty()){
            throw new UserException("Item with ID number "+ itemRequest.getId()+" does not exist", 400);
        }
        if (!Objects.equals(foundItem.get().getUserId(), user.get().getId())){
            throw new UserException("Owner of item not found", 400);
        }
        itemRepository.deleteById(itemRequest.getId());
    }

    @Override
    public List<ItemResponse> viewAuctionedItems() {
        List<Item> auctionedItems = itemRepository.findAll();
        return auctionedItems.stream()
                .filter(item -> item.isAuctioned() && !item.isBought() && !item.isExpired())
                .map(item -> ItemResponse.builder()
                        .name(item.getName())
                        .description(item.getDescription())
                        .dateUploaded(item.getDateUploaded())
                        .closingDate(item.getClosingDate())
                        .id(item.getId())
                        .build())
                .toList();

    }

    @Override
    public ItemResponse findItemById(ItemRequest request) {
        ItemResponse response = new ItemResponse();
        Optional<Item> foundItems = itemRepository.findById(request.getId());
        if (foundItems.isEmpty()){
            throw new UserException("Item with id number "+ request.getId() + " not found", 400);
        }
        BeanUtils.copyProperties(foundItems.get(), response);
        return response;
    }
    @Override
    public BidResponse bidOnItem(BidRequest bidRequest) {
        Optional<Item> foundItem = itemRepository.findById(bidRequest.getItemId());
        if (foundItem.isEmpty()){
            throw new UserException("Item with id number "+ bidRequest.getItemId()+ " does not exist", 400);
        }
        Item item = foundItem.get();
        if (Objects.equals(item.getUserId(), bidRequest.getUserId())){
            throw new UserException("You cannot purchase your own item ", 400);
        }
        boolean expired = utils.isAuctionExpired(item.getDateUploaded(), item.getClosingDate());
        if (expired){
            throw new UserException("Auction for this item is closed", 400);
        }
        if (bidRequest.getPrice().compareTo(item.getStartPrice()) < 0){
            throw new UserException("Your bargain price is too low for this auction", 400);
        }
        Bid bid = Bid.builder()
                .userId(bidRequest.getUserId())
                .price(bidRequest.getPrice())
                .build();
        item.getPricedUsers().add(bid);
        messagingTemplate.convertAndSend("/topic/bids/" + bidRequest.getItemId(), new BidResponse("Bid placed"));
        bidRepository.save(bid);
        itemRepository.save(item);
        return new BidResponse("Bid placed successfully");
    }

    @Override
    public List<PurchasedResponse> purchasedItems(PurchasedRequest purchasedRequest) {
        Optional<User> foundUser = userRepository.findById(purchasedRequest.getUserId());
        if (foundUser.isEmpty()){
            throw new UserException("User with ID "+ purchasedRequest.getUserId()+" not found", 400);
        }
        User user = foundUser.get();
        return user.getBoughtItems().stream()
                .map(item -> PurchasedResponse.builder()
                        .name(item.getName())
                        .description(item.getDescription())
                        .boughtPrice(item.getBoughtPrice())
                        .build()).toList();
    }

    @Override
    public BidResponse updateBid(BidRequest bidRequest) {
        Optional<Item> foundItem = itemRepository.findById(bidRequest.getItemId());
        Optional<Bid> foundBid = bidRepository.findByUserId(bidRequest.getUserId());
        if (foundItem.isEmpty()){
            throw new UserException("Item with id number "+ bidRequest.getItemId()+" does not exist", 400);
        }
        if (!foundItem.get().isAuctioned()){
            throw new UserException("Item no longer auctioned", 400);
        }
        if (foundBid.isEmpty()){
            throw new UserException("User with id number "+bidRequest.getUserId()+" does not exist", 400);
        }
        Bid bid = foundBid.get();
        bid.setPrice(bidRequest.getPrice());
        bidRepository.save(bid);
        return new BidResponse("Bid updated successfully");
    }
}