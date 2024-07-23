package com.example.assignment01.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "total_money")
    private Float totalMoney;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<OrderDetail> orderDetails;
}
