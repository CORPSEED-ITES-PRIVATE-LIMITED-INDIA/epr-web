package com.epr.service;



import com.epr.dto.admin.enquiry.EnquiryRequestDto;
import com.epr.dto.admin.enquiry.EnquiryResponseDto;

import java.util.List;

public interface EnquiryService {

    EnquiryResponseDto findById(Long id);

    List<EnquiryResponseDto> findAllActive();

    void softDeleteEnquiry(Long id, Long userId);

    EnquiryResponseDto createEnquiry(EnquiryRequestDto enquiryRequestDto, String clientIp, String url);
}