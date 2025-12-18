package com.epr.dto.admin.review;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerReviewRequestDto {

    @NotBlank(message = "Customer name is required")
    @Size(max = 100)
    private String customerName;

    @Size(max = 150)
    private String customerDesignation;

    @Size(max = 255)
    private String customerCompany;

    @Size(max = 500)
    private String customerPhoto; // URL

    @NotBlank(message = "Review message is required")
    private String reviewMessage;

    @Min(1) @Max(5)
    @NotNull(message = "Rating is required (1-5)")
    private Integer rating;

    private Long serviceId; // Optional - link to specific service

    private Long blogId;    // Optional - link to specific blog

    private Integer isFeatured = 2; // 1 = featured, 2 = normal

    private Integer displayStatus = 1; // 1 = visible, 2 = hidden
}