package org.weaponplace.filters;

import lombok.Data;
import org.weaponplace.entities.Auction;
import org.weaponplace.products.ProductType;

@Data
public class AuctionFilter {
    float minPrice;
    float maxPrice;
    int quantity;
    ProductType productType;

    //Условия WeaponType должны быть опциональными.
    public boolean matches(Auction auction) {
        if (auction.getLastPrice() > minPrice
                && auction.getLastPrice() < maxPrice
                && (long) auction.getProducts().size() >= quantity) {
            if(productType!=null) {
                return auction.getProducts().stream().anyMatch(product -> product.getProductType().equals(productType));
            }
            return true;
        }
        return false;
    }
}
