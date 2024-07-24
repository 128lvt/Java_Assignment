package com.example.assignment01.services;

import com.example.assignment01.models.Category;
import com.example.assignment01.models.Product;
import com.example.assignment01.repositories.ProductRepository;
import com.example.assignment01.systems.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public Product findById(Integer productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("product", productId));
    }

    public Product save(Product newProduct) {
        return this.productRepository.save(newProduct);
    }

    public Product update(Integer productId, Product updateProduct) {
        return this.productRepository.findById(productId)
                .map(oldProduct -> {
                    oldProduct.setName(updateProduct.getName());
                    oldProduct.setDescription(updateProduct.getDescription());
                    oldProduct.setPrice(updateProduct.getPrice());
                    oldProduct.setUpdateDate(new Date());
                    return this.productRepository.save(oldProduct);
                })
                .orElseThrow(() -> new ObjectNotFoundException("product", productId));
    }

    public void delete(Integer productId) {
        Product deleteProduct = this.productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("product", productId));
        deleteProduct.removeAllImage();
        this.productRepository.deleteById(productId);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findProductsByCategory(Category category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }
}
