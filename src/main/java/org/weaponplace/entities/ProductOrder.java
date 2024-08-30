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
    Boolean confirmed;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;
}
