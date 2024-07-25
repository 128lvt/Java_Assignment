package com.project.assignment.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListProductResponse {
    private List<ProductResponse> products;
    private int totalPages;
    private int totalItems;
}
