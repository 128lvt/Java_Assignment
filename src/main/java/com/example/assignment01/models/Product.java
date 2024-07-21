package com.example.assignment01.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Double price;

    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_at")
    private Date updateDate;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void removeAllImage() {
        this.productImages.stream()
                .forEach(productImage -> {
                    productImage.setProduct(null);
                });
        this.productImages = new ArrayList<>();
    }
}
