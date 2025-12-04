// src/main/java/com/epr/dto/enquiry/EnquiryRequestDto.java
package com.epr.dto.admin.enquiry;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnquiryRequestDto {

    private String type;
    private String message;
     private String name;
     @Email private String email;

     private  String location;

     @Size(min = 10, max = 15)
     private String mobile;

     private String city;

    private String utmSource;
    private String utmMedium;
    private String utmCampaign;
    private String utmTerm;
    private String utmContent;
}