// src/main/java/com/epr/dto/enquiry/EnquiryResponseDto.java
package com.epr.dto.admin.enquiry;

import lombok.Data;

@Data
public class EnquiryResponseDto {
    private Long id;
    private String uuid;
    private String type;
    private String message;
    private String name;
    private String email;
    private String mobile;
    private  String location;
    private String city;
    private String url;
    private String ipAddress;
    private String utmSource;
    private String utmMedium;
    private String utmCampaign;
    private String createdAt;  // Now correctly receives String
    private int displayStatus;
    private int deleteStatus;
    private Long count;
}