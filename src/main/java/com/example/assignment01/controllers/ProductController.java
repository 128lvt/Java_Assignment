package com.example.assignment01.controllers;

import com.example.assignment01.models.Category;
import com.example.assignment01.models.Product;
import com.example.assignment01.models.ProductImage;
import com.example.assignment01.services.CategoryService;
import com.example.assignment01.services.ProductImageService;
import com.example.assignment01.services.ProductService;
import com.example.assignment01.systems.ObjectNotFoundException;
import com.example.assignment01.utilities.FileUploader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/dash-board/products")
public class ProductController {


    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductImageService productImageService;

    public ProductController(ProductService productService, CategoryService categoryService, ProductImageService productImageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productImageService = productImageService;
    }

    @GetMapping
    public String showProduct(Model model) {
        List<Product> items = this.productService.findAll();
        model.addAttribute("items", items);
        return "dashboard/product";
    }

    @GetMapping("/news")
    public String showForm(Model model) {
        List<Category> categories = this.categoryService.findAll();
        model.addAttribute("item", new Product());
        model.addAttribute("categories", categories);
        return "dashboard/product_form";
    }

    @PostMapping("/save")
    public String addProduct(@ModelAttribute Product product,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("file") MultipartFile file,
                             Model model) {
        try {
            // Store the uploaded file and get the unique file name
            String folder = "product_images"; // specify your folder here
            String imageUrl = FileUploader.storeFile(file, folder);

            // Save the product entity
            product.setCreateDate(new Date());
            this.productService.save(product);

            // Find the existing ProductImage or create a new one
            Integer productId = product.getId();
            ProductImage pImg = this.productImageService.findByProductId(productId);
            if (pImg == null) {
                pImg = new ProductImage();
                pImg.setProduct(product);
            }

            // Set the URL of the image
            pImg.setUrl(imageUrl);

            // Save the ProductImage entity
            this.productImageService.save(pImg);

            redirectAttributes.addFlashAttribute("message", "The product has been saved successfully");
            return "redirect:/dash-board/products";
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload the image");
            return "product-form"; // return to the form in case of error
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer productId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product editProduct = this.productService.findById(productId);
            model.addAttribute("item", editProduct);
            redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
            return "dashboard/product_form";
        } catch (ObjectNotFoundException exception){
            exception.printStackTrace();
        }
        return "redirect:/dash-board/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer categoryId) {
        this.productService.delete(categoryId);
        return "redirect:/dash-board/products";
    }


}
