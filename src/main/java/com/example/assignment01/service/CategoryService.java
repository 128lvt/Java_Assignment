package com.example.assignment01.service;

import com.example.assignment01.model.Category;
import com.example.assignment01.repository.CategoryRepository;
import com.example.assignment01.system.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }


    public Category save(Category category) {
        return this.categoryRepository.save(category);
    }

    public Category findById(Integer id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("category", id));
    }

    public Category update(Integer categoryId, Category updateCategory) {
        return this.categoryRepository.findById(categoryId)
                .map(oldCategory -> {
                    oldCategory.setName(updateCategory.getName());
                    Category updatedCategory = this.categoryRepository.save(oldCategory);
                    return updatedCategory;
                })
                .orElseThrow(() -> new ObjectNotFoundException("category", categoryId));

    }

    public void delete(Integer categoryId) {
        this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("category", categoryId));
        this.categoryRepository.deleteById(categoryId);
    }
}
