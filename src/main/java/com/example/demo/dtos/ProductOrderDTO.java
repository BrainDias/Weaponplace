package com.example.demo.dtos;

import com.example.demo.products.Product;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.Set;

public class ProductOrderDTO {
    Set<Product> productSet;
    Float price;
    boolean delivered;
    private Date createdAt;
}
