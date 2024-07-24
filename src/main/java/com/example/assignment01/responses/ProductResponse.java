package com.example.assignment01.responses;

import com.example.assignment01.models.Category;
import com.example.assignment01.models.ProductImage;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Integer id;

    private String name;

    private Float price;

    private String description;

    private Date createDate;

    private CategoryResponse category;

    private List<ImageResponse> images;
}
