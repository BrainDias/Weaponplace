package org.weaponplace.entities;

import org.weaponplace.filters.ProductFilter;
import org.weaponplace.products.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@EntityListeners(AuditingEntityListener.class)
@Data
@Entity
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    private Float rating;
    private Integer ratingsNumber;

//    @Email
    private String email;
    private String phoneNumber;

    private Boolean active;
    private Boolean orderHistoryHidden;
    @ElementCollection
    private Set<GrantedAuthority> authorities;

    @ElementCollection
    private List<Product> products;

    @ElementCollection
    private List<Product> wishList;
    @ElementCollection
    private List<ProductFilter> wishFilters;
    @ElementCollection
    private List<Product> wishNotifiedProducts;

    @OneToMany(mappedBy = "buyer")
    private List<ProductOrder> buyingOrders;
    @OneToMany(mappedBy = "seller")
    private List<ProductOrder> sellingOrders;

    @OneToMany(mappedBy = "owner")
    private Set<Auction> ownedAuctions;
    @OneToMany(mappedBy = "pretender")
    private Set<Auction> pretendingAuctions;

    @Lob
    private byte[] avatar;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Верните список ролей или прав доступа пользователя
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

}

