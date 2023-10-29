package com.example.demo.products;

import com.example.demo.entities.ImageEntity;

import java.util.Set;

public abstract class Weapon extends Product {

    Caliber caliber;
    Float weight;
    Integer length;
    Set<AccessoryType> accessories;
    Integer rateOfFire;
    Sight sight;
}
