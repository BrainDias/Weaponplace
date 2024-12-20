package org.weaponplace.controllers;

import org.weaponplace.dtos.AuctionDTO;
import org.weaponplace.entities.User;
import org.weaponplace.filters.AuctionFilter;
import org.weaponplace.mappers.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.weaponplace.services.interfaces.AuctionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auctions")
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
    public void placeBidToAuction(@AuthenticationPrincipal User pretender,@RequestParam Float price, @PathVariable Long auctionId){
        auctionService.placeBidToAuction(pretender,price, auctionId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<AuctionDTO> currentAuctions(@RequestBody AuctionFilter filter){
        return auctionService.currentAuctions(filter)
                .stream()
                .map(mapper::auctionToAuctionDto)
                .toList();
    }
}
