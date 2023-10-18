package com.example.demo.controllers;

import com.example.demo.dtos.AuctionDTO;
import com.example.demo.entities.User;
import com.example.demo.services.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    @PostMapping("/auction")
    public void createAuction(@AuthenticationPrincipal User owner,
                                        @RequestBody AuctionDTO dto,
                                        @RequestParam Float startPrice,
                                        @RequestParam String title){
        auctionService.createAuction(owner,dto,startPrice,title);
    }

    @PutMapping("/bid/{auction_id}")
    public HttpStatusCode placeBidToAuction(@AuthenticationPrincipal User pretender,@RequestParam Float price,@PathVariable Long auction_id){
        return auctionService.placeBidToAuction(pretender,price,auction_id);
    }
}
