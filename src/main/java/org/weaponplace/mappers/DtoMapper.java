package org.weaponplace.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.weaponplace.dtos.AuctionDTO;
import org.weaponplace.dtos.ProductDTO;
import org.weaponplace.dtos.ProductOrderDTO;
import org.weaponplace.dtos.ProfileDTO;
import org.weaponplace.entities.Auction;
import org.weaponplace.entities.ProductOrder;
import org.weaponplace.entities.User;
import org.weaponplace.products.Product;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    ProfileDTO userToDto(User user);
    ProductOrderDTO orderToDto(ProductOrder order);

    Product productDtoToProduct(ProductDTO product);

    Auction auctionDtoToAuction(AuctionDTO dto);

    AuctionDTO auctionToAuctionDto(Auction auction);
}
