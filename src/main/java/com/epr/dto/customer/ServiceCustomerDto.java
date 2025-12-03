// src/main/java/com/epr/dto/customer/ServiceCustomerDto.java
package com.epr.dto.customer;

import lombok.Data;

@Data
public class ServiceCustomerDto {
    private Long id;
    private String uuid;
    private String title;
    private String slug;
    private String shortDescription;
    private String bannerImage;
    private String thumbnail;
    private String videoUrl;

    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;

    private Long categoryId;
    private String categoryName;
    private String categorySlug;

    private Long subcategoryId;
    private String subcategoryName;
    private String subcategorySlug;

    private String postDate; // formatted IST
}