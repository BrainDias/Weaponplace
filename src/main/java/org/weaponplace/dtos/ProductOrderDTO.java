package org.weaponplace.dtos;

import org.weaponplace.entities.User;
import org.weaponplace.products.Product;

import java.time.Instant;
import java.util.Set;

public class ProductOrderDTO {
    Set<Product> productSet;
    Float price;
    private Instant createdAt;
    User buyer;
    User seller;
}
