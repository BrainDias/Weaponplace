package com.example.demo.controllers;

import com.example.demo.dtos.AuctionDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.services.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionService auctionService;
    private final DtoMapper mapper;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAuction(@AuthenticationPrincipal User owner,
                                        @RequestBody AuctionDTO dto){
        auctionService.createAuction(owner,mapper.auctionDtoToAuction(dto));
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("{auctionId}/bid")
    public void placeBidToAuction(@AuthenticationPrincipal User pretender,@RequestBody Float price,@PathVariable Long auctionId){
        auctionService.placeBidToAuction(pretender,price, auctionId);
    }
}
