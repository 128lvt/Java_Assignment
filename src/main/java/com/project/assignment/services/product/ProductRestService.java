package com.project.assignment.services.product;

import com.project.assignment.dtos.ProductDTO;
import com.project.assignment.dtos.ProductImageDTO;
import com.project.assignment.models.Category;
import com.project.assignment.models.Product;
import com.project.assignment.models.ProductImage;
import com.project.assignment.repositories.CategoryRepository;
import com.project.assignment.repositories.ProductImageRepository;
import com.project.assignment.repositories.ProductRepository;
import com.project.assignment.responses.CategoryResponse;
import com.project.assignment.responses.ImageResponse;
import com.project.assignment.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                .createDate(LocalDate.now())
                .build();
        return productRepository.save(product);
    }

    @Override
    public ProductImage createProductImage(int productId, ProductImageDTO productImageDTO) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
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
                .map(product -> ProductResponse.of(product,
                        convertToCategoryResponse(
                                product.getCategory()
                        ),
                        convertToImageResponse(
                                productImageRepository.findByProduct(product)
                        )
                ))
                .collect(Collectors.toList());
    }

    public List<ImageResponse> convertToImageResponse(List<ProductImage> productImages) {
        return productImages
                .stream()
                .map(productImage -> ImageResponse
                        .builder()
                        .id(productImage.getId())
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

        /*return ProductResponse
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
                .build();*/
        return ProductResponse.of(product,
                convertToCategoryResponse(
                        product.getCategory()
                ),
                convertToImageResponse(
                        productImageRepository.findByProduct(product)
                )
        );
    }

    @Override
    public ProductResponse updateProduct(int id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        product.setUpdateDate(LocalDate.now());

        productRepository.save(product);
        return ProductResponse.of(product,
                convertToCategoryResponse(
                        product.getCategory()
                ),
                convertToImageResponse(
                        productImageRepository.findByProduct(product)
                )
        );
    }

    @Override
    public void deleteProduct(int id) {
        Product exsitedProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
//        exsitedProduct.removeAllImage();
        productRepository.deleteById(id);
    }
}
