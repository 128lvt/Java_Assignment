package com.project.assignment.controllers;

import com.project.assignment.dtos.ProductDTO;
import com.project.assignment.dtos.ProductImageDTO;
import com.project.assignment.models.Product;
import com.project.assignment.models.ProductImage;
import com.project.assignment.repositories.ProductImageRepository;
import com.project.assignment.repositories.ProductRepository;
import com.project.assignment.responses.ListProductResponse;
import com.project.assignment.responses.ProductImageResponse;
import com.project.assignment.responses.ProductResponse;
import com.project.assignment.services.product.ProductRestService;
import com.project.assignment.services.product_image.ProductImageRestService;
import com.project.assignment.systems.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.assignment.utilities.FileUploader.storeFile;

@RestController
@RequestMapping("${api.prefix}/products")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductRestService productService;
    private final ProductImageRestService productImageRestService;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductRestService productRestService;

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessage = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }

            return ResponseEntity.ok().body(productRestService.createProduct(productDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@ModelAttribute List<MultipartFile> files, @PathVariable("id") int id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            //Nếu không có files thì tạo mảng rỗng
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGE_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("Cant upload more than " + ProductImage.MAXIMUM_IMAGE_PER_PRODUCT + " images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    if (file.getSize() == 0) {
                        continue;
                    }
                    // Kiểm tra kích thước file
                    if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10MB");
                    }

                    // Kiểm tra định dạng
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                    }

                    int size = productImageRepository.findByProduct(product).size();
                    if (size >= ProductImage.MAXIMUM_IMAGE_PER_PRODUCT) {
                        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Maximum size is " + ProductImage.MAXIMUM_IMAGE_PER_PRODUCT + " images"));
                    }
                    //Lưu file và cập nhật thumbnail trong DTO
                    String fileName = storeFile(file, "product_images");
                    ProductImage productImage = productImageRestService.createProductImage(
                            product.getId(),
                            ProductImageDTO
                                    .builder()
                                    .url(fileName)
                                    .build()
                    );
                    productImages.add(productImage);
                }
            }
            List<ProductImageResponse> productImageResponses = productImages
                    .stream()
                    .map(ProductImageResponse::of)
                    .toList();
            return ResponseEntity.ok().body(productImageResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        List<Product> products = productService.getAllProducts(pageRequest).getContent();
        //Convert sang productresponse
        int totalPages = productRestService.getAllProducts(pageRequest).getTotalPages();
        int totalElements = (int) productRestService.getAllProducts(pageRequest).getTotalElements();

        List<ProductResponse> productResponses = productService.productResponseList(products);


        return ResponseEntity.ok().body(ListProductResponse
                .builder()
                .products(productResponses)
                .totalPages(totalPages)
                .totalItems(totalElements)
                .build());
    }

    @GetMapping("/images")
    public ResponseEntity<?> getAllProductImages() {
        List<ProductImageResponse> productImageResponses = productImageRestService.getAllProductImages();
        return ResponseEntity.ok(productImageResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") int id) {
        try {
            ProductResponse productResponse = productService.getProduct(id);
            return ResponseEntity.ok(productResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Get image by product
    @GetMapping("/images/")
    public ResponseEntity<?> getProductImage(@RequestParam("productId") int id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
            List<ProductImage> productImages = productImageRepository.findByProduct(product);
            List<ProductImageResponse> productImageResponses = productImages
                    .stream()
                    .map(ProductImageResponse::of)
                    .toList();
            return ResponseEntity.ok().body(productImageResponses);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<?> getProductImageById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok().body(productImageRestService.getProductImageById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") int id, @Valid @RequestBody ProductDTO productDTO) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, productDTO));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/images/{id}")
    public ResponseEntity<?> updateProductImage(@PathVariable("id") int id, List<MultipartFile> files) {
        try {
            //Nếu không có files thì tạo mảng rỗng
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > 1) {
                return ResponseEntity.badRequest().body("Failed");
            }
            List<ProductImage> productImages = new ArrayList<>();
            String fileName = null;
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    if (file.getSize() == 0) {
                        continue;
                    }
                    // Kiểm tra kích thước file
                    if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10MB");
                    }

                    // Kiểm tra định dạng
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                    }
                    //Lưu file và cập nhật thumbnail trong DTO
                    fileName = storeFile(file, "product_images");
                }
            }

            return ResponseEntity.ok().body(productImageRestService.updateProductImage(id, fileName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<?> deleteProductImage(@PathVariable("id") int id) {
        productImageRepository.deleteById(id);
        return ResponseEntity.ok().body(Collections.singletonMap("message", "Product image deleted"));
    }

}
