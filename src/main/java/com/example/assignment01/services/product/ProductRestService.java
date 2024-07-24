package com.example.assignment01.services.product;

import com.example.assignment01.dtos.ProductDTO;
import com.example.assignment01.models.Category;
import com.example.assignment01.models.Product;
import com.example.assignment01.models.ProductImage;
import com.example.assignment01.repositories.CategoryRepository;
import com.example.assignment01.repositories.ProductImageRepository;
import com.example.assignment01.repositories.ProductRepository;
import com.example.assignment01.responses.CategoryResponse;
import com.example.assignment01.responses.ImageResponse;
import com.example.assignment01.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductRestService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = Product
                .builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .category(category)
                .createDate(new Date())
                .build();
        return productRepository.save(product);
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

   /* public List<ProductResponse> convertToProductResponse(List<Product> products) {
        return products.stream().map(this::convertToProductResponse).collect(Collectors.toList());
    }*/


    /* Convert Product -> ProductResponse và convert ProductImage -> ImageResponse -> để không lấy các dữ liệu không cần thiết */

    public List<ProductResponse> productResponseList(List<Product> products) {

        return products
                .stream()
                .map(product -> ProductResponse
                        .builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .createDate(product.getCreateDate())
                        .updateDate(product.getUpdateDate())
                        .category(
                                convertToCategoryResponse(product.getCategory())
                        )
                        .images(
                                convertToImageResponse(
                                        productImageRepository
                                                .findByProduct(product))
                        )
                        .build())
                .collect(Collectors.toList());
    }

    public List<ImageResponse> convertToImageResponse(List<ProductImage> productImages) {
        return productImages
                .stream()
                .map(productImage -> ImageResponse
                        .builder()
                        .url(productImage.getUrl())
                        .build())
                .collect(Collectors.toList());
    }

    public CategoryResponse convertToCategoryResponse(Category category) {
        return CategoryResponse
                .builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .build();
    }

    /*private ProductResponse convertToProductResponse(Product product) {
        List<ProductImage> productImages = productImageRepository.findByProduct(product);
        List<ImageResponse> imageResponses = convertToImageResponse(productImageRepository.findByProduct(product));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .createDate(product.getCreateDate())
                .category(product.getCategory())
                .images(imageResponses)
                .build();
    }*/


    @Override
    public ProductResponse getProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .category(
                        convertToCategoryResponse(product.getCategory())
                )
                .images(
                        convertToImageResponse(
                                productImageRepository
                                        .findByProduct(product))
                )
                .build();
    }

    @Override
    public Product updateProduct(int id, ProductDTO productDTO) {
        Product exsitedProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

        exsitedProduct.setName(productDTO.getName());
        exsitedProduct.setPrice(productDTO.getPrice());
        exsitedProduct.setDescription(productDTO.getDescription());
        exsitedProduct.setCategory(category);
        exsitedProduct.setUpdateDate(new Date());

        productRepository.save(exsitedProduct);
        return exsitedProduct;
    }

    @Override
    public void deleteProduct(int id) {
        Product exsitedProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
//        exsitedProduct.removeAllImage();
        productRepository.deleteById(id);
    }
}
