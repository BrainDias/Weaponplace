package org.weaponplace.filters;

import jakarta.persistence.Embeddable;
import org.weaponplace.products.*;

@Embeddable
public class ProductFilter {
    ProductType productType;
    Caliber caliber;
    AccessoryType accessoryType;
    float maxPrice;
    WeaponType weaponType;
    String name;

    public  boolean matches(Product product) {
        ProductType pretendingProductType = product.getProductType();
        if(pretendingProductType.equals(productType) && product.getPrice()<=maxPrice && product.getName().contains(name)) {
            switch (pretendingProductType) {
                case AMMO -> {
                    return product.getCaliber().equals(caliber);
                }
                case OILS_AND_LUBRICANTS -> {
                    return  true;
                }
                case WEAPON -> {
                    return weaponType.equals(product.getWeaponType());
                }
                case ACCESSORY -> {
                    return accessoryType.equals(product.getAccessoryType());
                }
            }
        }
        return false;
    }
}
