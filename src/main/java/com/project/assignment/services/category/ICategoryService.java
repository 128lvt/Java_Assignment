package com.project.assignment.services.category;

import com.project.assignment.dtos.CategoryDTO;
import com.project.assignment.models.Category;
import com.project.assignment.responses.CategoryResponse;
import com.project.assignment.systems.NotFoundException;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTo);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(int id) throws NotFoundException;

    CategoryResponse updateCategory(CategoryDTO categoryDTo);

    void deleteCategory(int id);
}
