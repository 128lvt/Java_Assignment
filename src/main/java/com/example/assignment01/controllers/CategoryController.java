package com.example.assignment01.controllers;


import com.example.assignment01.model.Category;
import com.example.assignment01.service.CategoryService;
import com.example.assignment01.system.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dash-board/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showCategories(Model model) {
        Category item = new Category();
        model.addAttribute("item", item);
        List<Category> items = this.categoryService.findAll();
        model.addAttribute("items", items);
        return "dashboard/category";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute Category category, Model model) {
        model.addAttribute("item", category);
        this.categoryService.save(category);
        return "redirect:/dash-board/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model model) {
        Category item = this.categoryService.findById(id);
        model.addAttribute("item", item);
        List<Category> items = this.categoryService.findAll();
        model.addAttribute("items", items);
        return "dashboard/category";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category) {
        if (category.getId() != null) {
            this.categoryService.save(category);
        } else {
            throw new ObjectNotFoundException("category", category.getId());
        }
        return "redirect:/dash-board/categories/edit/" + category.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer categoryId) {
        this.categoryService.delete(categoryId);
        return "redirect:/dash-board/categories";
    }
}
