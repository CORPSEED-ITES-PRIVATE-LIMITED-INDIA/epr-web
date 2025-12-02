// src/main/java/com/epr/service/ServiceSectionService.java
package com.epr.service;

import com.epr.dto.servicesection.ServiceSectionRequestDto;
import com.epr.dto.servicesection.ServiceSectionResponseDto;

import java.util.List;

public interface ServiceSectionService {

    List<ServiceSectionResponseDto> findByServiceId(Long serviceId);

    ServiceSectionResponseDto findById(Long id);

    ServiceSectionResponseDto createSection(ServiceSectionRequestDto dto, Long userId);

    ServiceSectionResponseDto updateSection(Long id, ServiceSectionRequestDto dto, Long userId);

    void softDeleteSection(Long id, Long userId);
}