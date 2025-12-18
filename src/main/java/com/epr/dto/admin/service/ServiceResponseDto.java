package com.epr.dto.admin.service;

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

    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;

    private int displayStatus;
    private int showHomeStatus;

    private String postDate;
    private String modifyDate;
    private Long visited;
    private int deleteStatus;

    private Long categoryId;
    private String categoryName;
    private Long subcategoryId;
    private String subcategoryName;

    // ADD THESE TWO FIELDS
    private int showInFooter;
    private int footerOrder;

}