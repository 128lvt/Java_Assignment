package com.example.assignment01.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    @Column(length = 100)
    private String password;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Date create;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_at")
    private Date update;

    @Column(name = "is_active")
    private boolean enabled;

    private String username;

    @Column(name = "is_admin")
    private boolean roles;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Order> orders = new ArrayList<>();
}
