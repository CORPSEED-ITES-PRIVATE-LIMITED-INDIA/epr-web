package com.epr.dto.admin.servicesection;

import lombok.Data;

@Data
public class ServiceSectionResponseDto {
    private Long id;
    private String uuid;
    private String tabName;
    private String title;
    private String description;
    private String displayOrder;
    private Integer displayStatus;
    private Long serviceId;
    private String serviceTitle;
    private int deleteStatus;
}