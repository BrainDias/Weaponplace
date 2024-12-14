package org.weaponplace.services.interfaces;

import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;
import org.weaponplace.entities.Auction;
import org.weaponplace.entities.User;
import org.weaponplace.filters.AuctionFilter;

import java.util.List;

public interface AuctionService {

    @Transactional
    void createAuction(User owner, Auction auction);

    void closeAuctions();

    @Transactional
    void endAuctions(Streamable<Auction> auctionsToClose);

    void placeBidToAuction(User pretender, Float price, Long id);

    List<Auction> currentAuctions(AuctionFilter filter);
}
