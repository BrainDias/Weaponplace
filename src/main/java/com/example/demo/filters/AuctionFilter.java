package com.example.demo.filters;

import com.example.demo.entities.Auction;
import com.example.demo.products.Product;
import com.example.demo.products.ProductType;

public class AuctionFilter {
    float minPrice;
    float maxPrice;
    int quantity;
    ProductType productType;

    //Условия WeaponType должны быть опциональными.
    public boolean matches(Auction auction) {
        if (auction.getLastPrice() > minPrice && auction.getLastPrice() < maxPrice && auction.getProducts().stream().count()==quantity) {
            if(productType!=null) {
                return auction.getProducts().stream().allMatch(product -> product.getProductType().equals(productType));
            }
            return true;
        }
        return false;
    }
}
