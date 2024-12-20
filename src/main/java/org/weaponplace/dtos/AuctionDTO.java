package org.weaponplace.dtos;

import lombok.Getter;
import org.weaponplace.products.Product;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Getter
public class AuctionDTO {
    List<Product> products;
    String description;
    String title;
    Float startPrice;
    Float priceStep;
    Float lastPrice;
    Instant closingAt;

    public Float getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Float lastPrice) {
        this.lastPrice = lastPrice;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Float startPrice) {
        this.startPrice = startPrice;
    }

    public Float getPriceStep() {
        return priceStep;
    }

    public void setPriceStep(Float priceStep) {
        this.priceStep = priceStep;
    }

    public Instant getClosingAt() {
        return closingAt;
    }

    public void setClosingAt(Instant closingAt) {
        this.closingAt = closingAt;
    }
}
