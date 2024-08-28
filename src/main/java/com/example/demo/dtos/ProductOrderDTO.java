package com.example.demo.dtos;

import com.example.demo.products.Product;

import java.time.Instant;
import java.util.Set;

public class ProductOrderDTO {
    //TODO: добавить как-то строку покупателя и продавца
    Set<Product> productSet;
    Float price;
    Boolean delivered;
    private Instant createdAt;
}
