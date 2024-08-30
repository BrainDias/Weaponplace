package org.weaponplace.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.weaponplace.products.*;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    Float price;
    String name;
    Integer qty;

    boolean forSale;

    boolean hidden;
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
}
