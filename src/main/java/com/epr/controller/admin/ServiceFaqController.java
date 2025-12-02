package com.epr.controller.admin;

import com.epr.dto.admin.servicefaq.ServiceFaqRequestDto;
import com.epr.dto.admin.servicefaq.ServiceFaqResponseDto;
import com.epr.error.ApiResponse;
import com.epr.service.ServiceFaqService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-faqs")
public class ServiceFaqController {

    private static final Logger log = LoggerFactory.getLogger(ServiceFaqController.class);

    @Autowired
    private ServiceFaqService faqService;

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ServiceFaqResponseDto>> getByService(@PathVariable Long serviceId) {
        List<ServiceFaqResponseDto> list = faqService.findByServiceId(serviceId);
        return list.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceFaqResponseDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(faqService.findById(id));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ServiceFaqRequestDto dto,
                                    @RequestParam Long userId) {
        try {
            return new ResponseEntity<>(faqService.createFaq(dto, userId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating FAQ", e);
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to create FAQ"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam Long userId,
                                    @Valid @RequestBody ServiceFaqRequestDto dto) {
        try {
            return ResponseEntity.ok(faqService.updateFaq(id, dto, userId));
        } catch (IllegalArgumentException e) {
            HttpStatus status = e.getMessage().toLowerCase().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating FAQ", e);
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to update FAQ"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long userId) {
        try {
            faqService.softDeleteFaq(id, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            HttpStatus status = e.getMessage().toLowerCase().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting FAQ", e);
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to delete FAQ"));
        }
    }
}