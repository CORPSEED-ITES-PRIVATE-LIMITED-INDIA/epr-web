// src/main/java/com/epr/dto/admin/PlatformRatingRequestDto.java
package com.epr.dto.admin.rating;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlatformRatingRequestDto {

    @NotBlank
    @Size(max = 50)
    private String platform;

    @NotBlank
    @Size(max = 100)
    private String platformDisplayName;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("5.0")
    private Double rating;

    @Min(0)
    private Integer totalReviews = 0;

    @Size(max = 255)
    private String reviewUrl;

    @Size(max = 255)
    private String iconUrl;

    private Integer displayStatus = 1;
}