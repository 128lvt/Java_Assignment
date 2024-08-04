package com.project.assignment.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/dash-board")
    public String showAdminDashboard() {
        return "dashboard/layout-admin";
    }

    @GetMapping("/products")
    public String showAdminProducts() {
        return "admin/crud-product";
    }
    @GetMapping("/categories")
    public String showAdminCategories() {
        return "admin/crud-category";
    }

    @GetMapping("/products/edit-image")
    public String showAdminProductEditImage() {
        return "admin/edit-image";
    }
}
