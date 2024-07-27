package com.project.assignment.responses;

import com.project.assignment.models.Category;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Integer categoryId;
    private String categoryName;

    public static CategoryResponse of(Category category) {
        return CategoryResponse
                .builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .build();
    }
}
