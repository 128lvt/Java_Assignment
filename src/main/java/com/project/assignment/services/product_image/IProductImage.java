package com.project.assignment.services.product_image;

import com.project.assignment.dtos.ProductImageDTO;
import com.project.assignment.models.Product;
import com.project.assignment.models.ProductImage;
import com.project.assignment.responses.ProductImageResponse;

import java.util.List;

public interface IProductImage {
    ProductImage createProductImage(ProductImageDTO productImageDTO);
    List<ProductImageResponse> getAllProductImages();
    List<ProductImageResponse> getProductImageByProduct(Product product);

}
