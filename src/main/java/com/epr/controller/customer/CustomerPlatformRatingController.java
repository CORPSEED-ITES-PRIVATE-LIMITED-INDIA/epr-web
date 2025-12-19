// src/main/java/com/epr/controller/customer/CustomerPlatformRatingController.java
package com.epr.controller.customer;

import com.epr.dto.customer.PlatformRatingDto;
import com.epr.service.PlatformRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/platform-ratings")  // Same level as /blogs
@CrossOrigin(origins = "*")
public class CustomerPlatformRatingController {

    @Autowired
    private PlatformRatingService platformRatingService;

    /**
     * GET /platform-ratings
     * Public API - returns only active & visible ratings (e.g. Google, Trustpilot)
     * Perfect for showing rating badges on website header/footer
     */
    @GetMapping
    public ResponseEntity<List<PlatformRatingDto>> getAllVisibleRatings() {
        List<PlatformRatingDto> ratings = platformRatingService.findAllActiveAndVisible();
        return ResponseEntity.ok(ratings);
    }
}