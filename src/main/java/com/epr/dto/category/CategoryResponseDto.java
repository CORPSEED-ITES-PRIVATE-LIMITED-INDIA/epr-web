// src/main/java/com/epr/dto/category/CategoryResponseDto.java
package com.epr.dto.category;

import lombok.Data;

@Data
public class CategoryResponseDto {
    private Long id;
    private String uuid;
    private String name;
    private String slug;
    private String icon;
    private Integer sequence;
    private String displayStatus;
    private String showHomeStatus;

    // SEO
    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;
    private String searchKeywords;

    private String postDate;
    private String modifyDate;
    private int deleteStatus;
}