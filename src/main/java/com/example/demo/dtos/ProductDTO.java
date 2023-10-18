package com.example.demo.dtos;

import com.example.demo.entities.ImageEntity;
import com.example.demo.products.*;

import java.util.Set;

public class ProductDTO {
    Caliber caliber;
    Float weight;
    Integer length;
    Set<AccessoryType> accessories;

    Sight sight;
    public Float price;

    public boolean forSale;

    public ImageEntity image;
    Integer quantity;
    String ammoType;
    Integer barrelLength;
    Integer rateOfFire;
    FeedingType feedingType;
}
