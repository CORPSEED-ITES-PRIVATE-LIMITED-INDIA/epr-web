package com.epr.dto.admin.rating;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlatformRatingResponseDto {
    private Long id;
    private String platform;
    private String platformDisplayName;
    private Double rating;
    private Integer totalReviews;
    private String reviewUrl;
    private String iconUrl;
    private int displayStatus;
    private int deleteStatus;
    private String createdAt;
    private String updatedAt;
}