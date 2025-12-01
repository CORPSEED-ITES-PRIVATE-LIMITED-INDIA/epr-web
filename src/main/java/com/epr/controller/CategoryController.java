// src/main/java/com/epr/controller/CategoryController.java
package com.epr.controller;

import com.epr.dto.category.CategoryRequestDto;
import com.epr.dto.category.CategoryResponseDto;
import com.epr.error.ApiResponse;  // <-- Using your shared class
import com.epr.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllActiveCategories() {
        List<CategoryResponseDto> categories = categoryService.findAllActiveCategories();
        return categories.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(categoryService.findById(id));
        } catch (IllegalArgumentException e) {
            log.warn("Category not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryRequestDto dto,
            @RequestParam Long userId) {

        log.info("Creating category by userId={} | Request: {}", userId, dto);

        try {
            CategoryResponseDto saved = categoryService.createCategory(dto, userId);
            log.info("Category created successfully: {}", saved.getName());
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Validation failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error while creating category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create category. Please try again later.",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestParam Long userId,
            @Valid @RequestBody CategoryRequestDto dto) {

        log.info("Updating category ID={} by userId={}", id, userId);

        try {
            CategoryResponseDto updated = categoryService.updateCategory(id, dto, userId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            log.error("Update failed: {}", e.getMessage());
            boolean isNotFound = e.getMessage() != null && e.getMessage().toLowerCase().contains("not found");
            HttpStatus status = isNotFound ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .body(ApiResponse.error(e.getMessage(), status.value()));
        } catch (Exception e) {
            log.error("Unexpected error while updating category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update category.",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            @RequestParam Long userId) {

        log.info("Soft deleting category ID={} by userId={}", id, userId);

        try {
            categoryService.softDeleteCategory(id, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            log.error("Delete failed: {}", e.getMessage());
            boolean isNotFound = e.getMessage() != null && e.getMessage().toLowerCase().contains("not found");
            HttpStatus status = isNotFound ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .body(ApiResponse.error(e.getMessage(), status.value()));
        } catch (Exception e) {
            log.error("Unexpected error while deleting category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete category.",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}