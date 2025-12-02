// src/main/java/com/epr/dto/category/CategoryResponseDto.java
package com.epr.dto.admin.category;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponseDto {
    private Long id;
    private String uuid;
    private String name;
    private String slug;
    private String icon;
    private String displayStatus;
    private String showHomeStatus;

    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;
    private String searchKeywords;

    private LocalDateTime postDate;
    private LocalDateTime modifyDate;
    private int deleteStatus;
    private String addedByUUID;
    private String modifyByUUID;

}