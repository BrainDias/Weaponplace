package com.example.demo.controllers;

import com.example.demo.dtos.AuctionDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.products.ProductType;
import com.example.demo.services.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    public void placeBidToAuction(@AuthenticationPrincipal User pretender,@RequestParam Float price, @PathVariable Long auctionId){
        auctionService.placeBidToAuction(pretender,price, auctionId);
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/")
    public Page<AuctionDTO> currentAuctions(@RequestParam Float minPrice, @RequestParam Float maxPrice, @RequestParam ProductType productType,
                                            @RequestParam(defaultValue = "1") Integer quantity){
        return new PageImpl<>(new ArrayList<>());//TODO: сделать фильтр и возврат текущих аукционов, соответствующих условиям. Условия Weapon Type должны быть опциональными
    }
}
