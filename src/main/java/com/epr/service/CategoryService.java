// src/main/java/com/epr/service/CategoryService.java
package com.epr.service;

import com.epr.dto.admin.category.CategoryRequestDto;
import com.epr.dto.admin.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAllActiveCategories();
    CategoryResponseDto findById(Long id);
    CategoryResponseDto createCategory(CategoryRequestDto dto, Long userId);
    CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto, Long userId);
    void softDeleteCategory(Long id, Long userId);
}