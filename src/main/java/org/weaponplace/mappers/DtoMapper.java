package org.weaponplace.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    //@Mapping(target = "authorities", expression = "java(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))")
//    @Mapping(source = "email", target = "email")
//    @Mapping(source = "active", target = "active")
//    @Mapping(source = "avatar", target = "avatar")
    ProfileDTO userToDto(User user);

    @Mapping(source = "buyer.username", target = "buyer")
    @Mapping(source = "seller.username", target = "seller")
    ProductOrderDTO orderToDto(ProductOrder order);

    Product productDtoToProduct(ProductDTO product);

    ProductDTO productToDto(Product product);

    Auction auctionDtoToAuction(AuctionDTO dto);

    AuctionDTO auctionToAuctionDto(Auction auction);
}
