package com.project.assignment.services.product_image;

import com.project.assignment.dtos.ProductImageDTO;
import com.project.assignment.models.Product;
import com.project.assignment.models.ProductImage;
import com.project.assignment.responses.ProductImageResponse;
import com.project.assignment.systems.NotFoundException;

import java.util.List;

public interface IProductImage {
    ProductImage createProductImage(int productId, ProductImageDTO productImageDTO) throws NotFoundException;

    List<ProductImageResponse> getAllProductImages();

    List<ProductImageResponse> getProductImageByProduct(Product product) throws NotFoundException;

    ProductImageResponse getProductImageById(int id) throws NotFoundException;

    ProductImageResponse updateProductImage(int id, String fileName) throws NotFoundException;

    void deleteProductImageById(int id);
}
