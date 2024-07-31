package com.project.assignment.services.product;

import com.project.assignment.dtos.ProductDTO;
import com.project.assignment.dtos.ProductImageDTO;
import com.project.assignment.models.Product;
import com.project.assignment.models.ProductImage;
import com.project.assignment.responses.ProductResponse;
import com.project.assignment.systems.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    ProductResponse createProduct(ProductDTO productDTO) throws NotFoundException;

/*    ProductImage createProductImage(int productId, ProductImageDTO productImageDTO);*/

    Page<Product> getAllProducts(PageRequest pageRequest);

    ProductResponse getProduct(int id) throws NotFoundException;

    ProductResponse updateProduct(int id, ProductDTO productDTO) throws NotFoundException;

    void deleteProduct(int id);
}
