package com.project.assignment.responses;

import com.project.assignment.models.ProductImage;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponse {
    private Integer productImageId;
    private Integer productId;
    private String url;

    public static ProductImageResponse of(ProductImage productImage) {
        return ProductImageResponse
                .builder()
                .productImageId(productImage.getId())
                .productId(productImage.getProduct().getId())
                .url(productImage.getUrl())
                .build();
    }
}
