package com.epr.dto.customer;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlatformRatingDto {
    private Long id;
    private String platform;
    private String platformDisplayName;
    private Double rating;
    private Integer totalReviews;
    private String reviewUrl;
    private String iconUrl;
}