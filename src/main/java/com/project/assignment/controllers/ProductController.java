package com.project.assignment.controllers;

import com.project.assignment.models.Category;
import com.project.assignment.models.Product;
import com.project.assignment.models.ProductImage;
import com.project.assignment.services.CategoryService;
import com.project.assignment.services.ProductImageService;
import com.project.assignment.services.ProductService;
import com.project.assignment.systems.ObjectNotFoundException;
import com.project.assignment.utilities.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/dash-board/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductImageService productImageService;

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
            product.setCreateDate(LocalDate.now());
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
            return "dashboard/product_form"; // return to the form in case of error
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer productId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product editProduct = this.productService.findById(productId);
            model.addAttribute("item", editProduct);
            redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
            return "dashboard/product_form";
        } catch (ObjectNotFoundException exception) {
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
