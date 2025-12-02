// src/main/java/com/epr/dto/service/ServiceRequestDto.java
package com.epr.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServiceRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Slug is required")
    @Size(max = 250)
    private String slug;

    private Long categoryId;
    private Long subcategoryId;

    @Size(max = 500)
    private String shortDescription;

    private String fullDescription;
    private String bannerImage;
    private String thumbnail;
    private String videoUrl;

    // SEO
    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;

    private Integer displayStatus = 1;      // default show
    private Integer showHomeStatus = 2;     // default hide on home
}