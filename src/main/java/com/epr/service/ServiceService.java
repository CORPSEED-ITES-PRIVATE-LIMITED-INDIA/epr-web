// src/main/java/com/epr/service/ServiceService.java
package com.epr.service;

import com.epr.dto.service.ServiceRequestDto;
import com.epr.dto.service.ServiceResponseDto;

import java.util.List;

public interface ServiceService {

    List<ServiceResponseDto> findAllActiveServices();

    ServiceResponseDto findById(Long id);

    ServiceResponseDto createService(ServiceRequestDto dto, Long userId);

    ServiceResponseDto updateService(Long id, ServiceRequestDto dto, Long userId);

    void softDeleteService(Long id, Long userId);


    List<ServiceResponseDto> searchServices(String keyword);
}