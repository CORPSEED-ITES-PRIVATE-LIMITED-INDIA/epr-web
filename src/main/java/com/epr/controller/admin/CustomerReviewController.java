package com.epr.controller.admin;

import com.epr.dto.admin.review.CustomerReviewCustomerDto;
import com.epr.dto.admin.review.CustomerReviewRequestDto;
import com.epr.dto.admin.review.CustomerReviewResponseDto;
import com.epr.error.ApiResponse;
import com.epr.service.CustomerReviewService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class CustomerReviewController {

    private static final Logger log = LoggerFactory.getLogger(CustomerReviewController.class);

    @Autowired
    private CustomerReviewService reviewService;

    // =============== ADMIN ===============

    @GetMapping("/admin")
    public ResponseEntity<List<CustomerReviewResponseDto>> getAllAdmin() {
        return ResponseEntity.ok(reviewService.getAllAdminReviews());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CustomerReviewRequestDto dto,
                                    @RequestParam Long userId) {
        try {
            return new ResponseEntity<>(reviewService.createReview(dto, userId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody CustomerReviewRequestDto dto,
                                    @RequestParam Long userId) {
        try {
            return ResponseEntity.ok(reviewService.updateReview(id, dto, userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(e.getMessage().contains("not found") ? 404 : 400)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long userId) {
        try {
            reviewService.softDeleteReview(id, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    // =============== PUBLIC ===============

    @GetMapping("/featured")
    public ResponseEntity<List<CustomerReviewCustomerDto>> getFeatured(@RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(reviewService.getPublicFeaturedReviews(limit));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerReviewCustomerDto>> getAllPublic() {
        return ResponseEntity.ok(reviewService.getAllPublicVisibleReviews());
    }

}