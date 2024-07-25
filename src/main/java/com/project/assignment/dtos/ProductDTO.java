package com.project.assignment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import groovy.transform.builder.Builder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private String name;

    private Float price;

    private String description;

    @JsonProperty("category_id")
    @NotNull(message = "category id is required")
    @Min(value = 1, message = "Category id must be >= 1")
    private Integer categoryId;
}