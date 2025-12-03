// src/main/java/com/epr/serviceimpl/ServiceServiceImpl.java
package com.epr.serviceimpl;

import com.epr.dto.admin.service.ServiceRequestDto;
import com.epr.dto.admin.service.ServiceResponseDto;
import com.epr.dto.customer.ServiceCustomerDto;
import com.epr.entity.Category;
import com.epr.entity.Services;
import com.epr.entity.Subcategory;
import com.epr.entity.User;
import com.epr.repository.*;
import com.epr.service.ServiceService;
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
public class ServiceServiceImpl implements ServiceService {

    private static final Logger log = LoggerFactory.getLogger(ServiceServiceImpl.class);

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final DateTimeUtil dateTimeUtil;

    private User validateAndGetActiveUser(Long userId) {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("User ID is required");
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found or inactive"));
    }

    @Override
    public List<ServiceResponseDto> findAllActiveServices() {
        return serviceRepository.findAllActiveServices()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponseDto> searchServices(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return findAllActiveServices();
        return serviceRepository.searchActiveServices(keyword.trim())
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceResponseDto findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid service ID");
        Services service = serviceRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));
        return toResponseDto(service);
    }

    @Override
    public ServiceResponseDto createService(ServiceRequestDto dto, Long userId) {
        validateDto(dto);
        User currentUser = validateAndGetActiveUser(userId);

        String title = dto.getTitle().trim();
        String slug = dto.getSlug().trim().toLowerCase();

        if (serviceRepository.existsByTitleIgnoreCase(title))
            throw new IllegalArgumentException("Service with this title already exists");
        if (serviceRepository.existsBySlugIgnoreCaseAndIdNot(slug, null))
            throw new IllegalArgumentException("Service slug already exists");

        Services service = new Services();
        mapRequestToEntity(dto, service);

        service.setUuid(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
        LocalDateTime now = dateTimeUtil.getCurrentUtcTime();
        service.setPostDate(now);
        service.setModifyDate(now);
        service.setAddedByUUID(currentUser.getUuid());
        service.setVisited(0L);
        service.setDeleteStatus(2);

        setCategoryAndSubcategory(service, dto.getCategoryId(), dto.getSubcategoryId());

        Services saved = serviceRepository.save(service);
        log.info("Service created: {} by user {}", saved.getTitle(), userId);
        return toResponseDto(saved);
    }

    @Override
    public ServiceResponseDto updateService(Long id, ServiceRequestDto dto, Long userId) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid service ID");

        validateDto(dto);
        User currentUser = validateAndGetActiveUser(userId);

        Services existing = serviceRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        String newTitle = dto.getTitle().trim();
        String newSlug = dto.getSlug().trim().toLowerCase();

        if (!existing.getTitle().equalsIgnoreCase(newTitle) &&
                serviceRepository.existsByTitleIgnoreCaseAndIdNot(newTitle, id))
            throw new IllegalArgumentException("Service with this title already exists");
        if (!existing.getSlug().equalsIgnoreCase(newSlug) &&
                serviceRepository.existsBySlugIgnoreCaseAndIdNot(newSlug, id))
            throw new IllegalArgumentException("Service slug already exists");

        mapRequestToEntity(dto, existing);
        existing.setModifyDate(dateTimeUtil.getCurrentUtcTime());
        existing.setModifyByUUID(currentUser.getUuid());

        setCategoryAndSubcategory(existing, dto.getCategoryId(), dto.getSubcategoryId());

        Services updated = serviceRepository.save(existing);
        log.info("Service updated: {} (ID: {})", updated.getTitle(), updated.getId());
        return toResponseDto(updated);
    }

    @Override
    public void softDeleteService(Long id, Long userId) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid service ID");
        validateAndGetActiveUser(userId);

        Services service = serviceRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        service.setDeleteStatus(1);
        service.setModifyDate(dateTimeUtil.getCurrentUtcTime());
        serviceRepository.save(service);
        log.info("Service soft deleted: {} by user {}", id, userId);
    }

    // Helpers
    private void validateDto(ServiceRequestDto dto) {
        if (dto == null) throw new IllegalArgumentException("Service data is required");
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("Service title is required");
        if (dto.getSlug() == null || dto.getSlug().trim().isEmpty())
            throw new IllegalArgumentException("Service slug is required");
        if (dto.getCategoryId() == null || dto.getCategoryId() <= 0)
            throw new IllegalArgumentException("Valid category is required");
    }

    private void setCategoryAndSubcategory(Services service, Long categoryId, Long subcategoryId) {
        Category category = categoryRepository.findByIdAndDeleteStatus(categoryId, 2)
                .orElseThrow(() -> new IllegalArgumentException("Category not found or deleted"));
        service.setCategory(category);

        if (subcategoryId != null && subcategoryId > 0) {
            Subcategory sub = subcategoryRepository.findByIdAndDeleteStatus(subcategoryId, 2)
                    .orElseThrow(() -> new IllegalArgumentException("Subcategory not found or deleted"));
            if (!sub.getCategory().getId().equals(categoryId))
                throw new IllegalArgumentException("Subcategory does not belong to selected category");
            service.setSubcategory(sub);
        } else {
            service.setSubcategory(null);
        }
    }

    private void mapRequestToEntity(ServiceRequestDto dto, Services entity) {
        entity.setTitle(dto.getTitle().trim());
        entity.setSlug(dto.getSlug().trim().toLowerCase());
        entity.setShortDescription(dto.getShortDescription());
        entity.setFullDescription(dto.getFullDescription());
        entity.setBannerImage(dto.getBannerImage());
        entity.setThumbnail(dto.getThumbnail());
        entity.setVideoUrl(dto.getVideoUrl());

        entity.setMetaTitle(dto.getMetaTitle());
        entity.setMetaKeyword(dto.getMetaKeyword());
        entity.setMetaDescription(dto.getMetaDescription());

        entity.setDisplayStatus(dto.getDisplayStatus() != null ? dto.getDisplayStatus() : 1);
        entity.setShowHomeStatus(dto.getShowHomeStatus() != null ? dto.getShowHomeStatus() : 2);
    }

    private ServiceResponseDto toResponseDto(Services s) {
        ServiceResponseDto dto = new ServiceResponseDto();
        dto.setId(s.getId());
        dto.setUuid(s.getUuid());
        dto.setTitle(s.getTitle());
        dto.setSlug(s.getSlug());
        dto.setShortDescription(s.getShortDescription());
        dto.setFullDescription(s.getFullDescription());
        dto.setBannerImage(s.getBannerImage());
        dto.setThumbnail(s.getThumbnail());
        dto.setVideoUrl(s.getVideoUrl());
        dto.setMetaTitle(s.getMetaTitle());
        dto.setMetaKeyword(s.getMetaKeyword());
        dto.setMetaDescription(s.getMetaDescription());

        dto.setDisplayStatus(s.getDisplayStatus());
        dto.setShowHomeStatus(s.getShowHomeStatus());

        dto.setPostDate(dateTimeUtil.formatDateTimeIst(s.getPostDate()));
        dto.setModifyDate(s.getModifyDate() != null ? dateTimeUtil.formatDateTimeIst(s.getModifyDate()) : null);
        dto.setVisited(s.getVisited());
        dto.setDeleteStatus(s.getDeleteStatus());

        if (s.getCategory() != null) {
            dto.setCategoryId(s.getCategory().getId());
            dto.setCategoryName(s.getCategory().getName());
        }
        if (s.getSubcategory() != null) {
            dto.setSubcategoryId(s.getSubcategory().getId());
            dto.setSubcategoryName(s.getSubcategory().getName());
        }
        return dto;

    }


    @Override
    public List<ServiceCustomerDto> findAllActivePublicServices() {
        return serviceRepository.findAllActiveServices()
                .stream()
                .filter(s -> s.getDisplayStatus() == 1) // Only visible services
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceCustomerDto findActiveBySlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) return null;

        return serviceRepository.findBySlugIgnoreCaseAndDeleteStatusAndDisplayStatus(slug.trim().toLowerCase(), 2, 1)
                .map(this::toCustomerDto)
                .orElse(null);
    }

    @Override
    public List<ServiceCustomerDto> findActiveByCategoryId(Long categoryId) {
        if (categoryId == null || categoryId <= 0) return List.of();

        return serviceRepository.findByCategoryIdAndDeleteStatusAndDisplayStatus(categoryId, 2, 1)
                .stream()
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceCustomerDto> findActiveBySubcategoryId(Long subcategoryId) {
        if (subcategoryId == null || subcategoryId <= 0) {
            return List.of();
        }
        return serviceRepository.findBySubcategoryIdAndDeleteStatusAndDisplayStatus(subcategoryId, 2, 1)
                .stream()
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceCustomerDto> findLatestActiveServices(int limit) {
        return serviceRepository.findTopNActiveAndVisibleServices(limit)
                .stream()
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceCustomerDto> searchPublicServices(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAllActivePublicServices();
        }
        return serviceRepository.searchActivePublicServices(keyword.trim())
                .stream()
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceCustomerDto> findFeaturedServices() {
        return serviceRepository.findByShowHomeStatusAndDeleteStatusAndDisplayStatus(1, 2, 1)
                .stream()
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }


    private ServiceCustomerDto toCustomerDto(Services s) {
        ServiceCustomerDto dto = new ServiceCustomerDto();
        dto.setId(s.getId());
        dto.setUuid(s.getUuid());
        dto.setTitle(s.getTitle());
        dto.setSlug(s.getSlug());
        dto.setShortDescription(s.getShortDescription());
        dto.setBannerImage(s.getBannerImage());
        dto.setThumbnail(s.getThumbnail());
        dto.setVideoUrl(s.getVideoUrl());
        dto.setMetaTitle(s.getMetaTitle());
        dto.setMetaKeyword(s.getMetaKeyword());
        dto.setMetaDescription(s.getMetaDescription());
        dto.setPostDate(dateTimeUtil.formatDateTimeIst(s.getPostDate()));

        if (s.getCategory() != null) {
            dto.setCategoryId(s.getCategory().getId());
            dto.setCategoryName(s.getCategory().getName());
            dto.setCategorySlug(s.getCategory().getSlug());
        }
        if (s.getSubcategory() != null) {
            dto.setSubcategoryId(s.getSubcategory().getId());
            dto.setSubcategoryName(s.getSubcategory().getName());
            dto.setSubcategorySlug(s.getSubcategory().getSlug());
        }
        return dto;
    }


}