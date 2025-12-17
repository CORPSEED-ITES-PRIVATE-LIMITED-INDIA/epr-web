package com.epr.dto.customer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    private String postDate;

    private int showInFooter;
    private int footerOrder;
}