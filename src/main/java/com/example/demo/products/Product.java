package com.example.demo.products;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.Set;

@Embeddable
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Исключает null-поля при сериализации
public class Product {
    Float price;
    String name;
    Integer qty;

    boolean forSale;

    boolean hidden;

    @Lob
    private byte[] image;

    ProductType productType;

    //For sights only
    Float magnification;
    SightType type;

    //Weapons and ammo only
    Caliber caliber;

    //Weapons only
    Float weight;
    Integer length;
    Set<AccessoryType> accessories;
    Integer rateOfFire;
    WeaponType weaponType;
    //MG and sniper rifle only
    Integer barrelLength;
    //MG only
    FeedingType feedingType;

    //Ammo only
    String ammoType;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;
}
