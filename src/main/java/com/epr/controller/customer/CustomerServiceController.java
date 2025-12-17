// src/main/java/com/epr/controller/customer/CustomerServiceController.java
package com.epr.controller.customer;

import com.epr.dto.admin.servicefaq.ServiceFaqResponseDto;
import com.epr.dto.admin.servicesection.ServiceSectionResponseDto;
import com.epr.dto.customer.ServiceCustomerDto;
import com.epr.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "*") // Adjust in production
public class CustomerServiceController {


    @Autowired
    private ServiceService serviceService;

    // 1. Get all active & displayed services
    @GetMapping
    public ResponseEntity<List<ServiceCustomerDto>> getAllActiveServices() {
        List<ServiceCustomerDto> services = serviceService.findAllActivePublicServices();
        return services.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(services);
    }

    // 2. Get service by slug (SEO friendly URL: /service/web-development)
    @GetMapping("/{slug}")
    public ResponseEntity<ServiceCustomerDto> getServiceBySlug(@PathVariable String slug) {
        ServiceCustomerDto service = serviceService.findActiveBySlug(slug);
        return service != null
                ? ResponseEntity.ok(service)
                : ResponseEntity.notFound().build();
    }

    // 3. Get services by category ID (for category page)
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ServiceCustomerDto>> getServicesByCategory(@PathVariable Long categoryId) {
        List<ServiceCustomerDto> services = serviceService.findActiveByCategoryId(categoryId);
        return services.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(services);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ServiceCustomerDto>> searchServices(@RequestParam String q) {
        List<ServiceCustomerDto> services = serviceService.searchPublicServices(q.trim());
        return ResponseEntity.ok(services);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ServiceCustomerDto>> getFeaturedServices() {
        List<ServiceCustomerDto> services = serviceService.findFeaturedServices();
        return ResponseEntity.ok(services);
    }


    @GetMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<List<ServiceCustomerDto>> getServicesBySubcategory(@PathVariable Long subcategoryId) {
        List<ServiceCustomerDto> services = serviceService.findActiveBySubcategoryId(subcategoryId);
        return services.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(services);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<ServiceCustomerDto>> getLatestServices() {
        List<ServiceCustomerDto> services = serviceService.findLatestActiveServices(10);
        return ResponseEntity.ok(services);
    }


    /**
     * Get all sections (with cards) for a service by slug
     * Example: GET /services/web-development/sections
     */
    @GetMapping("/{slug}/sections")
    public ResponseEntity<List<ServiceSectionResponseDto>> getSectionsByServiceSlug(
            @PathVariable String slug) {

        try {
            List<ServiceSectionResponseDto> sections = serviceService.findSectionsByServiceSlug(slug);
            return sections.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(sections);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Get all active and visible FAQs for a service by slug
     * Example: GET /services/web-development/faqs
     */
    @GetMapping("/{slug}/faqs")
    public ResponseEntity<List<ServiceFaqResponseDto>> getFaqsByServiceSlug(
            @PathVariable String slug) {

        try {
            List<ServiceFaqResponseDto> faqs = serviceService.findFaqsByServiceSlug(slug);
            return faqs.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(faqs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


}