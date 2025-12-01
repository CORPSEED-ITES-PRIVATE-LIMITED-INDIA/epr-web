// src/main/java/com/epr/dto/service/ServiceResponseDto.java
package com.epr.dto.service;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServiceResponseDto {
    private Long id;
    private String uuid;
    private String title;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private String bannerImage;
    private String thumbnail;
    private String videoUrl;

    // SEO
    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;

    private int displayStatus;
    private int showHomeStatus;
    private Integer sequence;

    private String postDate;     // formatted IST
    private String modifyDate;   // formatted IST
    private Long visited;
    private int deleteStatus;

    private Long categoryId;
    private String categoryName;
    private Long subcategoryId;
    private String subcategoryName;
}