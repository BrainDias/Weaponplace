package org.weaponplace.entities;

import lombok.*;
import org.weaponplace.products.Product;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "product_orders")
@Getter
@Setter
//@ToString(exclude = {"buyer","seller"})
@NoArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    //@SequenceGenerator(name = "order_seq", sequenceName = "orders_id_seq")
    private Long id;

    @ManyToOne
            @JoinColumn(name = "buyer_id")
    private User buyer;
    @ManyToOne
            @JoinColumn(name = "seller_id")
    private User seller;

    @OneToMany(fetch = FetchType.EAGER)
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

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Float getPrice() {
        return price;
    }
}
