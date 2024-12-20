package org.weaponplace.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.weaponplace.products.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    Long id;

     Float price;
    String name;
    Integer qty;

    boolean forSale;
    boolean hidden;
    byte[] image;

     ProductType productType;

    // For sights only
    Float magnification;
     SightType sightType;

    // Weapons and ammo only
     Caliber caliber;

    // Weapons only
     Float weight;
     Integer length;
     AccessoryType accessoryType;
     Integer rateOfFire;
     WeaponType weaponType;

    // MG and sniper rifle only
     Integer barrelLength;

    // MG only
     FeedingType feedingType;

    // Ammo only
     String ammoType;

    // Setters for all fields


    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public boolean isForSale() {
        return forSale;
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Float getMagnification() {
        return magnification;
    }

    public void setMagnification(Float magnification) {
        this.magnification = magnification;
    }

    public SightType getSightType() {
        return sightType;
    }

    public void setSightType(SightType type) {
        this.sightType = type;
    }

    public Caliber getCaliber() {
        return caliber;
    }

    public void setCaliber(Caliber caliber) {
        this.caliber = caliber;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public AccessoryType getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(AccessoryType accessoryType) {
        this.accessoryType = accessoryType;
    }

    public Integer getRateOfFire() {
        return rateOfFire;
    }

    public void setRateOfFire(Integer rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public Integer getBarrelLength() {
        return barrelLength;
    }

    public void setBarrelLength(Integer barrelLength) {
        this.barrelLength = barrelLength;
    }

    public FeedingType getFeedingType() {
        return feedingType;
    }

    public void setFeedingType(FeedingType feedingType) {
        this.feedingType = feedingType;
    }

    public String getAmmoType() {
        return ammoType;
    }

    public void setAmmoType(String ammoType) {
        this.ammoType = ammoType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
