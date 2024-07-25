package com.project.assignment.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Float price;

    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private LocalDate createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_at")
    private LocalDate updateDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void removeAllImage() {
        this.productImages
                .forEach(productImage -> {
                    productImage.setProduct(null);
                });
        this.productImages = new ArrayList<>();
    }

}
