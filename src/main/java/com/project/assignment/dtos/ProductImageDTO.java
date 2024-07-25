package com.project.assignment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.assignment.models.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
    @JsonProperty("img_url")
    private String url;

    @JsonProperty("product_id")
    private Integer productId;
}
