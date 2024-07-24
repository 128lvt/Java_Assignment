package com.example.assignment01.services.product;

import com.example.assignment01.dtos.ProductDTO;
import com.example.assignment01.models.Product;
import com.example.assignment01.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);

    Page<Product> getAllProducts(PageRequest pageRequest);

    ProductResponse getProduct(int id);

    Product updateProduct(int id, ProductDTO productDTO);

    void deleteProduct(int id);
}
