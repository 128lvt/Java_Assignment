package com.example.assignment01.services.product;

import com.example.assignment01.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product createProduct(Product product);

    Page<Product> getAllProducts(PageRequest pageRequest);

    Product getProduct(int id);

    Product updateProduct(int id, Product product);

    void deleteProduct(int id);
}
