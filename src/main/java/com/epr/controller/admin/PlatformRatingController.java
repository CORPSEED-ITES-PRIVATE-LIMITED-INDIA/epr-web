package com.epr.controller.admin;


import com.epr.dto.admin.rating.PlatformRatingRequestDto;
import com.epr.dto.admin.rating.PlatformRatingResponseDto;
import com.epr.error.ApiResponse;
import com.epr.service.PlatformRatingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platform-ratings")
public class PlatformRatingController {

    private static final Logger log = LoggerFactory.getLogger(PlatformRatingController.class);

    @Autowired
    private PlatformRatingService platformRatingService;

    // GET: List all active ratings (for admin panel)
    @GetMapping
    public ResponseEntity<List<PlatformRatingResponseDto>> getAllActiveRatings() {
        List<PlatformRatingResponseDto> ratings = platformRatingService.findAllActiveForAdmin();
        return ratings.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : ResponseEntity.ok(ratings);
    }

    // GET: Single rating by ID
    @GetMapping("/{id}")
    public ResponseEntity<PlatformRatingResponseDto> getRatingById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(platformRatingService.findById(id));
        } catch (IllegalArgumentException e) {
            log.warn("Platform rating not found: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST: Create new rating
    @PostMapping
    public ResponseEntity<?> createRating(
            @Valid @RequestBody PlatformRatingRequestDto dto,
            @RequestParam Long userId) {

        log.info("Creating platform rating by userId={}", userId);
        try {
            PlatformRatingResponseDto saved = platformRatingService.createRating(dto, userId);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error creating platform rating", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create rating", 500));
        }
    }

    // PUT: Update existing rating
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRating(
            @PathVariable Long id,
            @Valid @RequestBody PlatformRatingRequestDto dto,
            @RequestParam Long userId) {

        log.info("Updating platform rating ID={} by userId={}", id, userId);
        try {
            PlatformRatingResponseDto updated = platformRatingService.updateRating(id, dto, userId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            boolean notFound = e.getMessage().toLowerCase().contains("not found");
            HttpStatus status = notFound ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .body(ApiResponse.error(e.getMessage(), status.value()));
        } catch (Exception e) {
            log.error("Unexpected error updating platform rating", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to update rating", 500));
        }
    }

    // DELETE: Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(
            @PathVariable Long id,
            @RequestParam Long userId) {

        log.info("Soft deleting platform rating ID={} by userId={}", id, userId);
        try {
            platformRatingService.softDeleteRating(id, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            boolean notFound = e.getMessage().toLowerCase().contains("not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error deleting platform rating", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to delete rating", 500));
        }
    }
}