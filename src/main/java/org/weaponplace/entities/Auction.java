package org.weaponplace.entities;

import org.weaponplace.products.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "auctions")
@Data
@NoArgsConstructor
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    //@SequenceGenerator(name = "auction_seq", sequenceName = "auctions_id_seq")
    private Long id;

    @ManyToOne
    User owner;
    @ManyToOne
    User pretender;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    private Boolean closed;

    private Float startPrice;
    private Float lastPrice;
    private Float priceStep;

    private String title; // Название аукциона
    private String description; // Описание аукциона

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant closingAt;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getClosingAt() {
        return closingAt;
    }

    public void setClosingAt(Instant closingAt) {
        this.closingAt = closingAt;
    }
}
