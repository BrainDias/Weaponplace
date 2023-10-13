package com.example.demo.products;

import com.example.demo.entities.ImageEntity;

import java.util.Set;

public interface Weapon extends Product {
    Caliber caliber = null;
    Float weight = null;
    Integer length = null;
    Set<Accessory> accessories = null;

    Sight sight = null;
    ImageEntity image = null;
}
