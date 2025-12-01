// src/main/java/com/epr/controller/SubcategoryController.java
package com.epr.controller;

import com.epr.dto.subcategory.SubcategoryRequestDto;
import com.epr.dto.subcategory.SubcategoryResponseDto;
import com.epr.error.ApiResponse;
import com.epr.service.SubcategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    private static final Logger log = LoggerFactory.getLogger(SubcategoryController.class);

    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping
    public ResponseEntity<List<SubcategoryResponseDto>> getAllActive() {
        List<SubcategoryResponseDto> list = subcategoryService.findAllActiveSubcategories();
        return list.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : ResponseEntity.ok(list);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SubcategoryResponseDto>> getByCategory(@PathVariable Long categoryId) {
        List<SubcategoryResponseDto> list = subcategoryService.findByCategoryId(categoryId);
        return list.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryResponseDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(subcategoryService.findById(id));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody SubcategoryRequestDto dto,
                                    @RequestParam Long userId) {
        try {
            SubcategoryResponseDto saved = subcategoryService.createSubcategory(dto, userId);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating subcategory", e);
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to create subcategory"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam Long userId,
                                    @Valid @RequestBody SubcategoryRequestDto dto) {
        try {
            return ResponseEntity.ok(subcategoryService.updateSubcategory(id, dto, userId));
        } catch (IllegalArgumentException e) {
            HttpStatus status = e.getMessage().toLowerCase().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating subcategory", e);
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to update subcategory"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long userId) {
        try {
            subcategoryService.softDeleteSubcategory(id, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            HttpStatus status = e.getMessage().toLowerCase().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting subcategory", e);
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to delete subcategory"));
        }
    }
}