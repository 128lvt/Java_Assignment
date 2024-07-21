package com.example.assignment01.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_img")
    private String url;

    @ManyToOne
    private Product product;
}
