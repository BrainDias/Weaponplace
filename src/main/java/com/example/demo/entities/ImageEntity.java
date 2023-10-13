package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] imageData;

    // Другие поля и методы
}

