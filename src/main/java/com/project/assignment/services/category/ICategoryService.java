package com.project.assignment.services.category;

import com.project.assignment.dtos.CategoryDTO;
import com.project.assignment.models.Category;
import com.project.assignment.responses.CategoryResponse;
import com.project.assignment.systems.NotFoundException;

import java.util.List;

public interface ICategoryService {
    CategoryResponse createCategory(CategoryDTO categoryDTO);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(int id) throws NotFoundException;

    CategoryResponse updateCategory(int id, CategoryDTO categoryDTO) throws NotFoundException;

    void deleteCategory(int id) throws NotFoundException;
}
