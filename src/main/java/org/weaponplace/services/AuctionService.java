package org.weaponplace.services;

import org.weaponplace.entities.Auction;
import org.weaponplace.entities.User;
import org.weaponplace.filters.AuctionFilter;
import org.weaponplace.products.Product;
import org.weaponplace.repositories.AuctionRepository;
import org.weaponplace.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public void createAuction(User owner, Auction auction) {
        List<Product> products = auction.getProducts();
        owner.getProducts().removeAll(products);
        userRepository.save(owner);
        auction.setOwner(owner);
        auction.setClosed(false);
        auctionRepository.save(auction);
    }
    @Scheduled(fixedDelay = 1,timeUnit = TimeUnit.MINUTES)
    public void closeAuctions(){
        Streamable<Auction> auctionsToClose = (Streamable<Auction>) auctionRepository.findToClose();
        endAuctions(auctionsToClose);
    }

    @Transactional
    public void endAuctions(Streamable<Auction> auctionsToClose) {
        auctionsToClose.forEach(auction -> {
            User pretender = auction.getPretender();
            List<Product> products = auction.getProducts();
            pretender.getProducts().addAll(products);
            auction.setClosed(true);
            userRepository.save(pretender);
            try {
                notificationService.notifyAuctionClosed(auction.getOwner(),pretender);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        auctionRepository.saveAll(auctionsToClose);
    }

    public void placeBidToAuction(User pretender, Float price, Long id) {
        auctionRepository.updateWithBid(pretender,id,price);
    }

    public List<Auction> currentAuctions(AuctionFilter filter) {
        return auctionRepository.findAllByClosedIsFalse().stream().filter(auction -> filter.matches(auction)).toList();
    }
}
