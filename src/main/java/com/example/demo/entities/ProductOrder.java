package com.example.demo.entities;

import com.example.demo.products.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
            @JoinColumn(name = "buyer_id")
    User buyer;
    @ManyToOne
            @JoinColumn(name = "seller_id")
    User seller;

    @ElementCollection
    Set<Product> productSet;

    Float price;
    boolean delivered;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
