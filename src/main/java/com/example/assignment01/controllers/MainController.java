package com.example.assignment01.controllers;

import com.example.assignment01.models.Category;
import com.example.assignment01.models.Product;
import com.example.assignment01.models.User;
import com.example.assignment01.services.CategoryService;
import com.example.assignment01.services.ProductService;
import com.example.assignment01.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping({"", "/"})
public class MainController {

    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;


    @Autowired
    public MainController(ProductService productService, UserService userService, CategoryService categoryService) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping({"", "/"})
    public String showIndex(Model model) {
        Pageable pageable = PageRequest.of(0, 8);
        Page<Product> page = this.productService.getAllProducts(pageable);
        model.addAttribute("productPage", page.getContent());
        return "index";
    }

    @GetMapping("/shop")
    public String showShopPage(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "-1") int category) {
        int pageSize = 6; // Number of items per page
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("name")); //name cua category

        Category categoryObj = new Category();
        categoryObj.setId(category);

        Page<Product> productPage = category != -1 ? productService.findProductsByCategory(categoryObj, pageable) : productService.getAllProducts(pageable);

        model.addAttribute("products", productPage.getContent());
        int maxVisiblePages = 7; // Maximum number of visible pages
        int startPage = Math.max(1, page - maxVisiblePages / 2);
        int endPage = Math.min(startPage + maxVisiblePages - 1, productPage.getTotalPages());

        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("categories", categoryService.findAll());

        return "shop";
    }

    @GetMapping("/shop-details")
    public String showShopDetailsPage(@RequestParam int productId, Model model) {
        Product product = productService.findById((Integer) productId);
        model.addAttribute("product", product);
        return "shop-details";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

    @GetMapping("/blog")
    public String showBlogPage() {
        return "blog";
    }

    @GetMapping("/blog-details")
    public String showBlogDetailsPage() {
        return "blog-details";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }

    @PostMapping("/register/save")
    public String registerUser(@ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        user.setCreate(new Date());
        user.setEnabled(true);
        user.setRoles(false);
        userService.save(user);
        return "redirect:/login";
    }
}
