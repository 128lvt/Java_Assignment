package com.project.assignment.services.product_image;

import com.project.assignment.dtos.ProductImageDTO;
import com.project.assignment.models.Product;
import com.project.assignment.models.ProductImage;
import com.project.assignment.repositories.ProductImageRepository;
import com.project.assignment.repositories.ProductRepository;
import com.project.assignment.responses.ProductImageResponse;
import com.project.assignment.responses.ProductResponse;
import com.project.assignment.services.ProductImageService;
import com.project.assignment.systems.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageRestService implements IProductImage {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductImage createProductImage(int productId, ProductImageDTO productImageDTO) throws NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        ProductImage productImage = ProductImage
                .builder()
                .product(product)
                .url(productImageDTO.getUrl())
                .build();
        int size = productImageRepository.findByProduct(product).size();
        if (size >= ProductImage.MAXIMUM_IMAGE_PER_PRODUCT) {
            throw new RuntimeException("Maximum image per product exceeded");
        }
        return productImageRepository.save(productImage);
    }

    @Override
    public List<ProductImageResponse> getAllProductImages() {
        List<ProductImage> productImages = productImageRepository.findAll();
        return productImages
                .stream()
                .map(ProductImageResponse::of)
                .toList();
    }

    @Override
    public List<ProductImageResponse> getProductImageByProduct(Product product) throws NotFoundException {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new NotFoundException("Product not found"));
        List<ProductImage> productImages = productImageRepository.findByProduct(existingProduct);
        return productImages
                .stream()
                .map(ProductImageResponse::of)
                .toList();
    }

    @Override
    public ProductImageResponse getProductImageById(int id) throws NotFoundException {
        ProductImage productImage = productImageRepository.findById(id).orElseThrow(() -> new NotFoundException("ProductImage not found"));
        return ProductImageResponse.of(productImage);
    }

    @Override
    public ProductImageResponse updateProductImage(int id, String fileName) throws NotFoundException {
        ProductImage productImage = productImageRepository.findById(id).orElseThrow(() -> new NotFoundException("ProductImage not found"));
        productImage.setUrl(fileName);
        productImageRepository.save(productImage);
        return ProductImageResponse.of(productImage);
    }

    @Override
    public void deleteProductImageById(int id) {
        productImageRepository.deleteById(id);
    }
}
