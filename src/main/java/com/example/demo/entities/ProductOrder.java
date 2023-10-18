package com.example.demo.entities;

import com.example.demo.products.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
    boolean delivered;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
