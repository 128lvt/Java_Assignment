package com.project.assignment.services.category;

import com.project.assignment.dtos.CategoryDTO;
import com.project.assignment.models.Category;
import com.project.assignment.repositories.CategoryRepository;
import com.project.assignment.responses.CategoryResponse;
import com.project.assignment.systems.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryRestService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryDTO categoryDTO) {
        Category category = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        return CategoryResponse.of(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryResponse::of).toList();
    }

    @Override
    public CategoryResponse getCategoryById(int id) throws NotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        return CategoryResponse.of(category);
    }

    @Override
    public CategoryResponse updateCategory(int id, CategoryDTO categoryDTO) throws NotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));

        category.setName(categoryDTO.getName());
        categoryRepository.save(category);
        return CategoryResponse.of(category);
    }

    @Override
    public void deleteCategory(int id) throws NotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.deleteById(id);
    }
}
