package com.project.assignment.controllers;

import com.project.assignment.services.category.CategoryRestService;
import com.project.assignment.systems.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CategoryRestController {
    private final CategoryRestService categoryRestService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok().body(categoryRestService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable int id) {
        try {
            return ResponseEntity.ok().body(categoryRestService.getCategoryById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
