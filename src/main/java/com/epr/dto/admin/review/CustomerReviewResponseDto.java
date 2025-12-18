package com.epr.dto.admin.review;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomerReviewResponseDto {

    private Long id;
    private String uuid;
    private String customerName;
    private String customerDesignation;
    private String customerCompany;
    private String customerPhoto;
    private String reviewMessage;
    private Integer rating;
    private String serviceTitle;   // if linked
    private String blogTitle;      // if linked
    private Integer isFeatured;
    private Integer displayStatus;
    private Integer deleteStatus;
    private String postDate;
    private String modifyDate;
    private String addedByName;    // optional, if you want to show who added
}