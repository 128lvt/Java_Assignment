package com.example.assignment01.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number_of_product")
    private Integer qty;

    private Float price;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;
}
