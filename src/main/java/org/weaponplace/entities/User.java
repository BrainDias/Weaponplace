package org.weaponplace.entities;

import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    //@SequenceGenerator(name = "user_seq", sequenceName = "users_id_seq")
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
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> authorities;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Product> wishList;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<ProductFilter> wishFilters;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Product> wishNotifiedProducts;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.EAGER)
    private List<ProductOrder> buyingOrders;
    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    private List<ProductOrder> sellingOrders;

//    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
//    private Set<Auction> ownedAuctions;
//    @OneToMany(mappedBy = "pretender", fetch = FetchType.EAGER)
//    private Set<Auction> pretendingAuctions;

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
        return authorities.stream().map(SimpleGrantedAuthority::new).toList();
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

    public String getEmail() {
        return email;
    }

    public Boolean getActive() {
        return active;
    }

    public byte[] getAvatar() {
        return avatar;
    }
}

