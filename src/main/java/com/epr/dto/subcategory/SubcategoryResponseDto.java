// src/main/java/com/epr/dto/subcategory/SubcategoryResponseDto.java
package com.epr.dto.subcategory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class SubcategoryResponseDto {
    private Long id;
    private String uuid;
    private String name;
    private String slug;
    private Long categoryId;
    private String categoryName;
    private Integer sequence;
    private String displayStatus;
    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;
    private LocalDateTime postDate;     // formatted IST
    private LocalDateTime modifyDate;   // formatted IST
    private int deleteStatus;
    private String addedByUUID;
    private String modifyByUUID;
}