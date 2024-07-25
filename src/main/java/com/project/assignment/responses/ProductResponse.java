package com.project.assignment.responses;

import com.project.assignment.models.Product;
import com.project.assignment.repositories.CategoryRepository;
import com.project.assignment.repositories.ProductImageRepository;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class ProductResponse {

    private Integer id;

    private String name;

    private Float price;

    private String description;

    private LocalDate createDate;

    private LocalDate updateDate;

    private CategoryResponse category;

    private List<ImageResponse> images;

    public static ProductResponse of(Product product, CategoryResponse category, List<ImageResponse> images) {
        return ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .category(
                        category
                )
                .images(
                        images
                )
                .build();
    }
}
