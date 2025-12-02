package com.epr.dto.servicefaq;

import lombok.Data;

@Data
public class ServiceFaqResponseDto {
    private Long id;
    private String uuid;
    private String question;
    private String answer;
    private Integer displayOrder;
    private Integer displayStatus;
    private Long serviceId;
    private String serviceTitle;
    private int deleteStatus;
}