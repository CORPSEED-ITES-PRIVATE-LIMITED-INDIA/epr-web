package com.epr.service;


import com.epr.dto.servicefaq.ServiceFaqRequestDto;
import com.epr.dto.servicefaq.ServiceFaqResponseDto;

import java.util.List;

public interface ServiceFaqService {

    List<ServiceFaqResponseDto> findByServiceId(Long serviceId);

    ServiceFaqResponseDto findById(Long id);

    ServiceFaqResponseDto createFaq(ServiceFaqRequestDto dto, Long userId);

    ServiceFaqResponseDto updateFaq(Long id, ServiceFaqRequestDto dto, Long userId);

    void softDeleteFaq(Long id, Long userId);
}