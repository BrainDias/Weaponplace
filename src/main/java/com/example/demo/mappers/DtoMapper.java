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

    Ammo productDtoToAmmo(ProductDTO product);

    AssaultRifle productDtoToAr(ProductDTO product);

    MachineGun productDtoToMachinegun(ProductDTO product);

    Pistol productDtoToPistol(ProductDTO product);

    SniperRifle productDtoToSniperRifle(ProductDTO product);

    Ammo productToAmmo(Product product);

    AssaultRifle productToAr(Product product);

    MachineGun productToMachinegun(Product product);

    Pistol productToPistol(Product product);

    SniperRifle productToSniperRifle(Product product);

    Auction auctionDtoToAuction(AuctionDTO dto);
}
