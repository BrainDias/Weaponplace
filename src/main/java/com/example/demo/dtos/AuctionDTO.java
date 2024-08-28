package com.example.demo.dtos;

import com.example.demo.products.Product;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AuctionDTO {
    List<Product> products;
    String description;
    String title;
    Float startPrice;
    Float priceStep;
    Instant closingAt;
}
