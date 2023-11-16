package com.example.DAuction.utils;

import com.example.DAuction.data.model.Bid;
import com.example.DAuction.data.model.Item;
import com.example.DAuction.data.model.Location;
import com.example.DAuction.data.model.User;
import com.example.DAuction.data.repositories.BidRepository;
import com.example.DAuction.data.repositories.ItemRepository;
import com.example.DAuction.data.repositories.UserRepository;
import com.example.DAuction.dto.request.UserRequest;
import com.example.DAuction.exception.UserException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class Utils {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;

    private final Logger logger = LoggerFactory.getLogger(Utils.class);
    public boolean isAuctionExpired(LocalDateTime datePosted, LocalDateTime endTime){
        return datePosted.isAfter(endTime);
    }
    public void validateDate(LocalDateTime startDate, LocalDateTime closeDate){
        long daysUntilClosing = ChronoUnit.DAYS.between(startDate, closeDate);
        if(daysUntilClosing > 7){
            throw new UserException("Closing date cannot be greater than 7 days from now", 400);
        }
    }
    public User register(UserRequest userRequest){
        Location location = Location.builder()
                .country(userRequest.getCountry())
                .state(userRequest.getState())
                .LGA(userRequest.getLGA())
                .build();

        User user = User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .location(location)
                .build();
        return userRepository.save(user);
    }
    @Scheduled(fixedRate = 60000) // Check every minute (adjust as needed)
    @Transactional
    public void processExpiredAuctions() {
        List<Item> expiredItems = itemRepository.findExpiredItems();

        for (Item item : expiredItems) {
            List<Bid> bids = bidRepository.findByItemOrderByPriceDesc(item);

            if (!bids.isEmpty()) {
                Bid winningBid = bids.get(0);
                Long winningUserId = winningBid.getUserId();

                Optional<User> foundUser = userRepository.findById(winningUserId);
                if (foundUser.isPresent()) {
                    User winningUser = foundUser.get();
                    winningUser.getBoughtItems().add(item);
                    Long ownerId = item.getUserId();
                    Optional<User> foundOwner = userRepository.findById(ownerId);
                    if (foundUser.isEmpty()){
                        throw new UserException("User with ID number "+ ownerId+" does not exist", 400);
                    }
                    User owner = foundOwner.get();
                    owner.getAuctionedItem().remove(item);
                    item.setAuctioned(false);
                    item.setBought(true);
                    item.setExpired(true);
                } else {
                    logger.warn("Winning user with ID "+ winningUserId+" not found for item with ID "+item.getId());
                }
            }
        }
    }
}
