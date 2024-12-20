package org.weaponplace.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.weaponplace.entities.Auction;
import org.weaponplace.repositories.AuctionRepository;
import org.weaponplace.services.interfaces.AuctionService;

import java.util.concurrent.TimeUnit;

//Created to avoid transactional self-invocation
@Service
@RequiredArgsConstructor
public class ScheduledTasksService {
    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;

    @Scheduled(fixedDelay = 1,timeUnit = TimeUnit.MINUTES)
    public void closeAuctions(){
        Streamable<Auction> auctionsToClose = (Streamable<Auction>) auctionRepository.findToClose();
        auctionService.endAuctions(auctionsToClose);
    }
}
