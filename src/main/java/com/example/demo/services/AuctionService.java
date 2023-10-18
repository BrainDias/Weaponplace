package com.example.demo.services;

import com.example.demo.dtos.AuctionDTO;
import com.example.demo.entities.Auction;
import com.example.demo.entities.User;
import com.example.demo.products.Product;
import com.example.demo.repositories.AuctionRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.demo.services.NotificationService.notifyAuctionClosed;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createAuction(User owner, AuctionDTO dto, Float startPrice, String title) {
        List<Product> products = dto.getProducts();
        owner.getProducts().removeAll(products);
        userRepository.save(owner);
        Auction auction = new Auction();
        auction.setClosed(false);
        auction.setOwner(owner);
        auction.setStartPrice(startPrice);
        auction.setProducts(products);
        auction.setTitle(title);
        auction.setDescription(dto.getDescription());
        auctionRepository.save(auction);
    }
    @Scheduled(fixedDelay = 1,timeUnit = TimeUnit.MINUTES)
    public void closeAuctions(){
        Streamable<Auction> auctions = (Streamable<Auction>) auctionRepository.findAll();
        Streamable<Auction> auctionsToClose = auctions.filter(auction -> auction.getClosingAt().before(new Date()));
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
                notifyAuctionClosed(auction.getOwner(),pretender);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        auctionRepository.saveAll(auctionsToClose);
    }

    public HttpStatusCode placeBidToAuction(User pretender, Float price, Long auction_id) {
        Optional<Auction> optionalAuction = auctionRepository.findById(auction_id);
        if(optionalAuction.isEmpty()) return HttpStatus.BAD_REQUEST;
        Auction auction = optionalAuction.get();
        if(auction.getLastPrice()>=price||auction.isClosed()) return HttpStatus.PRECONDITION_FAILED;
        auction.setPretender(pretender);
        auction.setLastPrice(price);
        auctionRepository.save(auction);
        return HttpStatus.ACCEPTED;
    }
}
