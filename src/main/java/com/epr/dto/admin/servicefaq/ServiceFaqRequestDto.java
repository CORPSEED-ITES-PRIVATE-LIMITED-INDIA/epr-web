package com.epr.dto.admin.servicefaq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServiceFaqRequestDto {
    @NotBlank private String question;
    @NotBlank private String answer;
    private Integer displayOrder;
    private Integer displayStatus;
    @NotNull private Long serviceId;
}