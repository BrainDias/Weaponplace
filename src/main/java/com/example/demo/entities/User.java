package com.example.demo.entities;

import com.example.demo.products.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

//    @Email
    private String email;
    private boolean active;
    private boolean orderHistoryHidden;
    @ElementCollection
    private Set<GrantedAuthority> authorities;
    @ElementCollection
    private List<Product> products;
    @OneToMany(mappedBy = "buyer")
    private Set<ProductOrder> buyingOrders;
    @OneToMany(mappedBy = "seller")
    private Set<ProductOrder> sellingOrders;
    @OneToMany(mappedBy = "owner")
    private Set<Auction> ownedAuctions;
    @OneToMany(mappedBy = "pretender")
    private Set<Auction> pretendingAuctions;
    @OneToOne(mappedBy = "user")
    private ImageEntity avatar;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
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

