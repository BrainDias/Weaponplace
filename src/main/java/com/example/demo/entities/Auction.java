package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

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

    private Float startPrice;
    private Float lastPrice;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date closingAt;
}
