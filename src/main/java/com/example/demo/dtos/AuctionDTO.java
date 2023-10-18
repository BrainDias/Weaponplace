package com.example.demo.dtos;

import com.example.demo.products.Product;
import lombok.Data;

import java.util.List;

@Data
public class AuctionDTO {
    List<Product> products;
    String description;
}
