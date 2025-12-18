package com.epr.dto.admin.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerReviewCustomerDto {

    private String customerName;
    private String customerDesignation;
    private String customerCompany;
    private String customerPhoto;
    private String reviewMessage;
    private Integer rating;
    private String serviceTitle;
    private String blogTitle;
}