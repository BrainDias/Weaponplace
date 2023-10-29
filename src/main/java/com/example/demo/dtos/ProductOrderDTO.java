package com.example.demo.dtos;

import com.example.demo.products.Product;

import java.time.Instant;
import java.util.Set;

public class ProductOrderDTO {
    Set<Product> productSet;
    Float price;
    Boolean delivered;
    private Instant createdAt;
}
