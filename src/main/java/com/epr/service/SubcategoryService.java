// src/main/java/com/epr/service/SubcategoryService.java
package com.epr.service;

import com.epr.dto.admin.subcategory.SubcategoryRequestDto;
import com.epr.dto.admin.subcategory.SubcategoryResponseDto;

import java.util.List;

public interface SubcategoryService {
    List<SubcategoryResponseDto> findAllActiveSubcategories();
    List<SubcategoryResponseDto> findByCategoryId(Long categoryId);
    SubcategoryResponseDto findById(Long id);
    SubcategoryResponseDto createSubcategory(SubcategoryRequestDto dto, Long userId);
    SubcategoryResponseDto updateSubcategory(Long id, SubcategoryRequestDto dto, Long userId);
    void softDeleteSubcategory(Long id, Long userId);
}