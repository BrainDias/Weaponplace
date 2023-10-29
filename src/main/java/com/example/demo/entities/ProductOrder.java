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
public class ProductOrder {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
            @JoinColumn(name = "buyer_id")
    private User buyer;
    @ManyToOne
            @JoinColumn(name = "seller_id")
    private User seller;

    @ElementCollection
    private List<Product> products;

    Float price;
    Boolean delivered;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;
}
