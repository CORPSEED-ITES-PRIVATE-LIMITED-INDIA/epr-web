// src/main/java/com/epr/controller/ServiceSectionController.java
package com.epr.controller.admin;

import com.epr.dto.admin.servicesection.ServiceSectionRequestDto;
import com.epr.dto.admin.servicesection.ServiceSectionResponseDto;
import com.epr.error.ApiResponse;
import com.epr.service.ServiceSectionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-sections")
public class ServiceSectionController {

    private static final Logger log = LoggerFactory.getLogger(ServiceSectionController.class);

    @Autowired
    private ServiceSectionService sectionService;

    // Get all sections for a service (ordered)
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ServiceSectionResponseDto>> getByService(@PathVariable Long serviceId) {
        List<ServiceSectionResponseDto> list = sectionService.findByServiceId(serviceId);
        return list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : ResponseEntity.ok(list);
    }

    //

    @GetMapping("/{id}")
    public ResponseEntity<ServiceSectionResponseDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(sectionService.findById(id));
        } catch (IllegalArgumentException e) {
            log.warn("Service section not found: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ServiceSectionRequestDto dto,
                                    @RequestParam Long userId) {
        try {
            ServiceSectionResponseDto saved = sectionService.createSection(dto, userId);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error creating section", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create section"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam Long userId,
                                    @Valid @RequestBody ServiceSectionRequestDto dto) {
        try {
            return ResponseEntity.ok(sectionService.updateSection(id, dto, userId));
        } catch (IllegalArgumentException e) {
            log.error("Update failed: {}", e.getMessage());
            HttpStatus status = e.getMessage().toLowerCase().contains("not found")
                    ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error updating section", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update section"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long userId) {
        try {
            sectionService.softDeleteSection(id, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            log.error("Delete failed: {}", e.getMessage());
            HttpStatus status = e.getMessage().toLowerCase().contains("not found")
                    ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error deleting section", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete section"));
        }
    }
}