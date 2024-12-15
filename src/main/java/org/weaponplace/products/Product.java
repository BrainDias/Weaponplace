package org.weaponplace.products;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Embeddable
@EntityListeners(AuditingEntityListener.class)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Исключает null-поля при сериализации
public class Product {
    Float price;
    String name;
    Integer qty;

    //For single accessories
    AccessoryType accessoryType;

    boolean forSale;

    boolean hidden;

    @Lob
    private byte[] image;

    ProductType productType;

    //For sights only
    Float magnification;
    SightType type;

    //Weapons and ammo only
    @Enumerated(EnumType.STRING)
    Caliber caliber;

    //Weapons only
    Float weight;
    Integer length;
    AccessoryType accessories;
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
