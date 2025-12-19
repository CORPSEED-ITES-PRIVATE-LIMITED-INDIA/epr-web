package com.epr.controller.customer;

import com.epr.dto.customer.BlogCustomerDto;
import com.epr.dto.customer.ServiceCustomerDto;
import com.epr.service.BlogService;
import com.epr.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client/footer")
public class FooterController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private BlogService blogService;

    /**
     * Get up to 8 services configured to show in the footer (ordered by footerOrder)
     * URL: GET /api/footer/services
     */
    @GetMapping("/services")
    public ResponseEntity<List<ServiceCustomerDto>> getFooterServices() {
        List<ServiceCustomerDto> footerServices = serviceService.findFooterServices();
        return footerServices.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(footerServices);
    }

    /**
     * Get up to 8 blogs configured to show in the footer (ordered by footerOrder)
     * URL: GET /api/footer/blogs
     */
    @GetMapping("/blogs")
    public ResponseEntity<List<BlogCustomerDto>> getFooterBlogs() {
        List<BlogCustomerDto> footerBlogs = blogService.findFooterBlogs();
        return footerBlogs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(footerBlogs);
    }
}