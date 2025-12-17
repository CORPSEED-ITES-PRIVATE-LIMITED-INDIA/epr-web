// src/main/java/com/epr/service/ServiceService.java
package com.epr.service;

import com.epr.dto.admin.service.ServiceRequestDto;
import com.epr.dto.admin.service.ServiceResponseDto;
import com.epr.dto.customer.ServiceCustomerDto;

import java.util.List;

public interface ServiceService {

    List<ServiceResponseDto> findAllActiveServices();

    ServiceResponseDto findById(Long id);

    ServiceResponseDto createService(ServiceRequestDto dto, Long userId);

    ServiceResponseDto updateService(Long id, ServiceRequestDto dto, Long userId);

    void softDeleteService(Long id, Long userId);


    List<ServiceResponseDto> searchServices(String keyword);

    List<ServiceCustomerDto> findAllActivePublicServices();
    ServiceCustomerDto findActiveBySlug(String slug);
    List<ServiceCustomerDto> findActiveByCategoryId(Long categoryId);
    List<ServiceCustomerDto> searchPublicServices(String keyword);
    List<ServiceCustomerDto> findFeaturedServices(); // showHomeStatus = 1


    List<ServiceCustomerDto> findActiveBySubcategoryId(Long subcategoryId);

    List<ServiceCustomerDto> findLatestActiveServices(int i);

    List<ServiceResponseDto> findActivePublicServicesBySubcategoryId(Long subcategoryId);


    List<ServiceCustomerDto> findFooterServices();
}