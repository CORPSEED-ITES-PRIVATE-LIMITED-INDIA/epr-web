// src/main/java/com/epr/dto/subcategory/SubcategoryRequestDto.java
package com.epr.dto.subcategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SubcategoryRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Slug is required")
    @Size(max = 200)
    private String slug;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Integer displayStatus = 1;

    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;
}