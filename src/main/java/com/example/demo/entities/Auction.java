package com.example.demo.entities;

import com.example.demo.products.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Auction {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;
    @ManyToOne
    @JoinColumn(name = "pretender_id")
    User pretender;

    @ElementCollection
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
}
