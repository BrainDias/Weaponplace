package org.weaponplace.filters;

import jakarta.persistence.Embeddable;
import lombok.Data;
import org.weaponplace.products.*;

@Embeddable
@Data
public class ProductFilter {
    ProductType productType;
    Caliber caliber;
    AccessoryType accessoryType;
    float maxPrice;
    WeaponType weaponType;
    String name;

    public  boolean matches(Product product) {
        ProductType pretendingProductType = product.getProductType();
        if((pretendingProductType.equals(productType)  || productType == null)
                && (product.getPrice()<=maxPrice || maxPrice == 0.0)
                && (name == null || product.getName().contains(name))
        ) {
            switch (pretendingProductType) {
                case AMMO -> {
                    return caliber == null || product.getCaliber().equals(caliber);
                }
                case OILS_AND_LUBRICANTS -> {
                    return  true;
                }
                case WEAPON -> {
                    return weaponType == null || weaponType.equals(product.getWeaponType());
                }
                case ACCESSORY -> {
                    return accessoryType == null || accessoryType.equals(product.getAccessoryType());
                }
            }
        }
        return false;
    }
}
