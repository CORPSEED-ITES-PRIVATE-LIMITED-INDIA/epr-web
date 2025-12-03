package com.epr.dto.customer;

import lombok.Data;

import java.util.List;

@Data
public class BlogCustomerDto {
    private Long id;
    private String uuid;
    private String title;
    private String slug;
    private String image;
    private String summary;
    private String description;
    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;

    private String postDate;
    private String postedByName;
    private Long visited;

    // Category
    private Long categoryId;
    private String categoryName;
    private String categorySlug;

    // Subcategory (optional)
    private Long subcategoryId;
    private String subcategoryName;
    private String subcategorySlug;

    // Related Services (titles only)
    private List<String> relatedServiceTitles;
}