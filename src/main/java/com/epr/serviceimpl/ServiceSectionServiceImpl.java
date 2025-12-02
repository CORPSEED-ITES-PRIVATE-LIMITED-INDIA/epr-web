// src/main/java/com/epr/serviceimpl/ServiceSectionServiceImpl.java
package com.epr.serviceimpl;

import com.epr.dto.servicesection.ServiceSectionRequestDto;
import com.epr.dto.servicesection.ServiceSectionResponseDto;
import com.epr.entity.ServiceSection;
import com.epr.entity.Services;
import com.epr.entity.User;
import com.epr.repository.ServiceRepository;
import com.epr.repository.ServiceSectionRepository;
import com.epr.repository.UserRepository;
import com.epr.service.ServiceSectionService;
import com.epr.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceSectionServiceImpl implements ServiceSectionService {

    private static final Logger log = LoggerFactory.getLogger(ServiceSectionServiceImpl.class);

    private final ServiceSectionRepository sectionRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final DateTimeUtil dateTimeUtil;

    private User validateAndGetActiveUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID is required");
        }
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found or inactive"));
    }

    @Override
    public List<ServiceSectionResponseDto> findByServiceId(Long serviceId) {
        if (serviceId == null || serviceId <= 0) {
            throw new IllegalArgumentException("Valid service ID is required");
        }
        return sectionRepository.findByServiceIdAndDeleteStatusOrderByDisplayOrderAsc(serviceId, 2).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceSectionResponseDto findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid section ID");
        }
        ServiceSection section = sectionRepository.findByIdAndDeleteStatus(id, 2)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));
        return toResponseDto(section);
    }

    @Override
    public ServiceSectionResponseDto createSection(ServiceSectionRequestDto dto, Long userId) {
        validateDto(dto);
        User user = validateAndGetActiveUser(userId);

        Services service = serviceRepository.findActiveById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found or deleted"));

        String tabName = dto.getTabName().trim();

        if (sectionRepository.existsByTabNameIgnoreCaseAndServiceId(tabName, dto.getServiceId())) {
            throw new IllegalArgumentException("Tab name already exists for this service");
        }

        ServiceSection section = new ServiceSection();
        mapRequestToEntity(dto, section);
        section.setService(service);
        section.setUuid(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
        section.setPostDate(dateTimeUtil.getCurrentUtcTime().toString());
        section.setAddedByUUID(user.getUuid());
        section.setDeleteStatus(2); // Active

        ServiceSection saved = sectionRepository.save(section);
        log.info("Service section created: {} for service {}", saved.getTabName(), service.getTitle());
        return toResponseDto(saved);
    }

    @Override
    public ServiceSectionResponseDto updateSection(Long id, ServiceSectionRequestDto dto, Long userId) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid section ID");
        }

        validateDto(dto);
        User user = validateAndGetActiveUser(userId);

        ServiceSection existing = sectionRepository.findByIdAndDeleteStatus(id, 2)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        Services service = serviceRepository.findActiveById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        String newTabName = dto.getTabName().trim();

        if (!existing.getTabName().equalsIgnoreCase(newTabName) &&
                sectionRepository.existsByTabNameIgnoreCaseAndServiceIdAndIdNot(newTabName, dto.getServiceId(), id)) {
            throw new IllegalArgumentException("Tab name already exists for this service");
        }

        mapRequestToEntity(dto, existing);
        existing.setService(service);
        existing.setModifyDate(dateTimeUtil.getCurrentUtcTime().toString());

        ServiceSection updated = sectionRepository.save(existing);
        log.info("Service section updated: {} (ID: {})", updated.getTabName(), updated.getId());
        return toResponseDto(updated);
    }

    @Override
    public void softDeleteSection(Long id, Long userId) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid section ID");
        }
        validateAndGetActiveUser(userId);

        ServiceSection section = sectionRepository.findByIdAndDeleteStatus(id, 2)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        if (!section.getCards().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete section that has cards");
        }

        section.setDeleteStatus(1); // Soft deleted
        section.setModifyDate(dateTimeUtil.getCurrentUtcTime().toString());
        sectionRepository.save(section);
        log.info("Service section soft deleted: {}", id);
    }

    // ================== Helpers ==================
    private void validateDto(ServiceSectionRequestDto dto) {
        if (dto == null) throw new IllegalArgumentException("Section data is required");
        if (dto.getTabName() == null || dto.getTabName().trim().isEmpty())
            throw new IllegalArgumentException("Tab name is required");
        if (dto.getServiceId() == null || dto.getServiceId() <= 0)
            throw new IllegalArgumentException("Valid service is required");
    }

    private void mapRequestToEntity(ServiceSectionRequestDto dto, ServiceSection entity) {
        entity.setTabName(dto.getTabName().trim());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        String order = (dto.getDisplayOrder() != null && !dto.getDisplayOrder().trim().isEmpty())
                ? dto.getDisplayOrder().trim() : "1";
        entity.setDisplayOrder(order);

        Integer status = dto.getDisplayStatus();
        entity.setDisplayStatus((status != null && (status == 1 || status == 2)) ? status : 1);
    }

    private ServiceSectionResponseDto toResponseDto(ServiceSection s) {
        ServiceSectionResponseDto dto = new ServiceSectionResponseDto();
        dto.setId(s.getId());
        dto.setUuid(s.getUuid());
        dto.setTabName(s.getTabName());
        dto.setTitle(s.getTitle());
        dto.setDescription(s.getDescription());
        dto.setDisplayOrder(s.getDisplayOrder());
        dto.setDisplayStatus(s.getDisplayStatus());
        dto.setServiceId(s.getService().getId());
        dto.setServiceTitle(s.getService().getTitle());
        dto.setDeleteStatus(s.getDeleteStatus());
        return dto;
    }
}