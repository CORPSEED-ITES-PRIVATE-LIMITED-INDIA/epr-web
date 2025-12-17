// src/main/java/com/epr/controller/customer/CustomerServiceController.java
package com.epr.controller.customer;

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

    // 5. NEW: Latest 10 recently posted services
    @GetMapping("/latest")
    public ResponseEntity<List<ServiceCustomerDto>> getLatestServices() {
        List<ServiceCustomerDto> services = serviceService.findLatestActiveServices(10);
        return ResponseEntity.ok(services); // Always return 200 + empty list if none
    }



}