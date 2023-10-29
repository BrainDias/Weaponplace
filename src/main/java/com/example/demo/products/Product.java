package com.example.demo.products;

import com.example.demo.entities.ImageEntity;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public abstract class Product {
    public Float price;

    public boolean forSale;

    public boolean hidden;

    public ImageEntity image;
}
