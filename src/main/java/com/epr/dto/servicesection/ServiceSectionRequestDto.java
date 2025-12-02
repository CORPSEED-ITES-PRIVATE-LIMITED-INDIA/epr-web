package com.epr.dto.servicesection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServiceSectionRequestDto {
    @NotBlank private String tabName;
    private String title;
    private String description;
    private String displayOrder;
    private Integer displayStatus;
    @NotNull private Long serviceId;
}