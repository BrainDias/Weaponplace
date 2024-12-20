package org.weaponplace.dtos;

import lombok.Getter;
import org.weaponplace.entities.User;
import org.weaponplace.products.Product;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
public class ProductOrderDTO {

    Long id;
    List<Product> products;
    Float price;
    Instant createdAt;
    String buyer;
    String seller;

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
