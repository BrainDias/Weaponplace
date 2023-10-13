package com.example.demo.mappers;

import com.example.demo.dtos.ProductOrderDTO;
import com.example.demo.dtos.ProfileDTO;
import com.example.demo.entities.ProductOrder;
import com.example.demo.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface DtoMapper {
    public ProfileDTO userToDto(User user);
    public ProductOrderDTO orderToDto(ProductOrder order);
}
