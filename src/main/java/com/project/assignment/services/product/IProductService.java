package com.project.assignment.services.product;

import com.project.assignment.dtos.ProductDTO;
import com.project.assignment.dtos.ProductImageDTO;
import com.project.assignment.models.Product;
import com.project.assignment.models.ProductImage;
import com.project.assignment.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);

    ProductImage createProductImage(int productId, ProductImageDTO productImageDTO);

    Page<Product> getAllProducts(PageRequest pageRequest);

    ProductResponse getProduct(int id);

    ProductResponse updateProduct(int id, ProductDTO productDTO);

    void deleteProduct(int id);
}
