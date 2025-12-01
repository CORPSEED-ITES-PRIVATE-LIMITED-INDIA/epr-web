// src/main/java/com/epr/serviceimpl/SubcategoryServiceImpl.java
package com.epr.serviceimpl;

import com.epr.dto.subcategory.SubcategoryRequestDto;
import com.epr.dto.subcategory.SubcategoryResponseDto;
import com.epr.entity.Category;
import com.epr.entity.Subcategory;
import com.epr.entity.User;
import com.epr.repository.CategoryRepository;
import com.epr.repository.SubcategoryRepository;
import com.epr.repository.UserRepository;
import com.epr.service.SubcategoryService;
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
public class SubcategoryServiceImpl implements SubcategoryService {

    private static final Logger log = LoggerFactory.getLogger(SubcategoryServiceImpl.class);

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final DateTimeUtil dateTimeUtil;

    private User validateAndGetActiveUser(Long userId) {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("User ID is required");
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found or inactive"));
    }

    @Override
    public List<SubcategoryResponseDto> findAllActiveSubcategories() {
        return subcategoryRepository.findByDeleteStatusOrderBySequenceAsc(2)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubcategoryResponseDto> findByCategoryId(Long categoryId) {
        return subcategoryRepository.findByCategoryIdAndDeleteStatusOrderBySequenceAsc(categoryId, 2)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubcategoryResponseDto findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid subcategory ID");
        Subcategory sub = subcategoryRepository.findByIdAndDeleteStatus(id, 2)
                .orElseThrow(() -> new IllegalArgumentException("Subcategory not found"));
        return toResponseDto(sub);
    }

    @Override
    public SubcategoryResponseDto createSubcategory(SubcategoryRequestDto dto, Long userId) {
        validateDto(dto);
        User user = validateAndGetActiveUser(userId);

        String name = dto.getName().trim();
        String slug = dto.getSlug().trim().toLowerCase();

        if (subcategoryRepository.existsByNameIgnoreCase(name))
            throw new IllegalArgumentException("Subcategory name already exists");
        if (subcategoryRepository.existsBySlugIgnoreCase(slug))
            throw new IllegalArgumentException("Subcategory slug already exists");

        Category category = categoryRepository.findByIdAndDeleteStatus(dto.getCategoryId(), 2)
                .orElseThrow(() -> new IllegalArgumentException("Category not found or deleted"));

        Subcategory sub = new Subcategory();
        mapRequestToEntity(dto, sub);
        sub.setCategory(category);
        sub.setUuid(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
        sub.setPostDate(dateTimeUtil.getCurrentUtcTime());

        sub.setDeleteStatus(2);

        Subcategory saved = subcategoryRepository.save(sub);
        log.info("Subcategory created: {} under category {}", saved.getName(), category.getName());
        return toResponseDto(saved);
    }

    @Override
    public SubcategoryResponseDto updateSubcategory(Long id, SubcategoryRequestDto dto, Long userId) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid subcategory ID");

        validateDto(dto);
        User user = validateAndGetActiveUser(userId);

        Subcategory existing = subcategoryRepository.findByIdAndDeleteStatus(id, 2)
                .orElseThrow(() -> new IllegalArgumentException("Subcategory not found"));

        String newName = dto.getName().trim();
        String newSlug = dto.getSlug().trim().toLowerCase();

        if (!existing.getName().equalsIgnoreCase(newName) &&
                subcategoryRepository.existsByNameIgnoreCaseAndIdNot(newName, id))
            throw new IllegalArgumentException("Subcategory name already exists");

        if (!existing.getSlug().equalsIgnoreCase(newSlug) &&
                subcategoryRepository.existsBySlugIgnoreCaseAndIdNot(newSlug, id))
            throw new IllegalArgumentException("Subcategory slug already exists");

        Category category = categoryRepository.findByIdAndDeleteStatus(dto.getCategoryId(), 2)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        mapRequestToEntity(dto, existing);
        existing.setCategory(category);
        existing.setModifyDate(dateTimeUtil.getCurrentUtcTime());

        Subcategory updated = subcategoryRepository.save(existing);
        return toResponseDto(updated);
    }

    @Override
    public void softDeleteSubcategory(Long id, Long userId) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid subcategory ID");
        validateAndGetActiveUser(userId);

        Subcategory sub = subcategoryRepository.findByIdAndDeleteStatus(id, 2)
                .orElseThrow(() -> new IllegalArgumentException("Subcategory not found"));

        if (!sub.getServices().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete subcategory that has services assigned");
        }

        sub.setDeleteStatus(1);
        sub.setModifyDate(dateTimeUtil.getCurrentUtcTime());
        subcategoryRepository.save(sub);
        log.info("Subcategory soft deleted: {}", id);
    }

    // Helpers
    private void validateDto(SubcategoryRequestDto dto) {
        if (dto == null) throw new IllegalArgumentException("Subcategory data is required");
        if (dto.getName() == null || dto.getName().trim().isEmpty())
            throw new IllegalArgumentException("Subcategory name is required");
        if (dto.getSlug() == null || dto.getSlug().trim().isEmpty())
            throw new IllegalArgumentException("Subcategory slug is required");
        if (dto.getCategoryId() == null || dto.getCategoryId() <= 0)
            throw new IllegalArgumentException("Valid category is required");
    }

    private void mapRequestToEntity(SubcategoryRequestDto dto, Subcategory entity) {
        entity.setName(dto.getName().trim());
        entity.setSlug(dto.getSlug().trim().toLowerCase());
        entity.setSequence(dto.getSequence() != null ? dto.getSequence() : 0);
        entity.setDisplayStatus(dto.getDisplayStatus() != null && !dto.getDisplayStatus().isBlank()
                ? Integer.parseInt(dto.getDisplayStatus().trim()) : 1);
        entity.setMetaTitle(dto.getMetaTitle());
        entity.setMetaKeyword(dto.getMetaKeyword());
        entity.setMetaDescription(dto.getMetaDescription());
    }

    private SubcategoryResponseDto toResponseDto(Subcategory s) {
        SubcategoryResponseDto dto = new SubcategoryResponseDto();
        dto.setId(s.getId());
        dto.setUuid(s.getUuid());
        dto.setName(s.getName());
        dto.setSlug(s.getSlug());
        dto.setSequence(s.getSequence());
        dto.setDisplayStatus(String.valueOf(s.getDisplayStatus()));
        dto.setMetaTitle(s.getMetaTitle());
        dto.setMetaKeyword(s.getMetaKeyword());
        dto.setMetaDescription(s.getMetaDescription());
        dto.setDeleteStatus(s.getDeleteStatus());

        if (s.getCategory() != null) {
            dto.setCategoryId(s.getCategory().getId());
            dto.setCategoryName(s.getCategory().getName());
        }

        return dto;
    }
}