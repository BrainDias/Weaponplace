package com.example.demo.mappers;

import com.example.demo.dtos.AuctionDTO;
import com.example.demo.dtos.ProductDTO;
import com.example.demo.dtos.ProductOrderDTO;
import com.example.demo.dtos.ProfileDTO;
import com.example.demo.entities.Auction;
import com.example.demo.entities.ProductOrder;
import com.example.demo.entities.User;
import com.example.demo.products.*;
import org.mapstruct.Mapper;

@Mapper
public interface DtoMapper {
    ProfileDTO userToDto(User user);
    ProductOrderDTO orderToDto(ProductOrder order);

    Product productDtoToProduct(ProductDTO product);

    Auction auctionDtoToAuction(AuctionDTO dto);
}
