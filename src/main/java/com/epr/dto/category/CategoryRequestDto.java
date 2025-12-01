// src/main/java/com/epr/dto/category/CategoryRequestDto.java
package com.epr.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Slug is required")
    @Size(max = 200, message = "Slug must not exceed 200 characters")
    private String slug;

    @Size(max = 255)
    private String icon;

    private Integer displayStatus = 1;

    private Integer showHomeStatus = 2;

    // SEO fields
    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;
    private String searchKeywords;
}