package com.example.assignment01.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_img")
@Getter
@Setter
public class ProductImage {
    public static final int MAXIMUM_IMAGE_PER_PRODUCT = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "img_url")
    private String url;

    @ManyToOne
    private Product product;
}
