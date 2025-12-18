package com.epr.service;

import com.epr.dto.admin.review.CustomerReviewCustomerDto;
import com.epr.dto.admin.review.CustomerReviewRequestDto;
import com.epr.dto.admin.review.CustomerReviewResponseDto;

import java.util.List;

public interface CustomerReviewService {

    // ====================== ADMIN METHODS ======================

    /**
     * Get all active reviews for admin dashboard.
     * @return List of admin DTOs
     */
    List<CustomerReviewResponseDto> getAllAdminReviews();

    /**
     * Create a new customer review (admin action).
     * @param dto Review data
     * @param userId Admin user ID performing the action
     * @return Created review DTO
     */
    CustomerReviewResponseDto createReview(CustomerReviewRequestDto dto, Long userId);

    /**
     * Update an existing review.
     * @param id Review ID
     * @param dto Updated data
     * @param userId Admin user ID
     * @return Updated review DTO
     */
    CustomerReviewResponseDto updateReview(Long id, CustomerReviewRequestDto dto, Long userId);

    /**
     * Soft delete a review.
     * @param id Review ID
     * @param userId Admin user ID
     */
    void softDeleteReview(Long id, Long userId);

    // ====================== PUBLIC / CUSTOMER METHODS ======================

    /**
     * Get featured reviews for homepage (limited number).
     * @param limit Max number to return (e.g., 6 for carousel)
     * @return List of public DTOs
     */
    List<CustomerReviewCustomerDto> getPublicFeaturedReviews(int limit);

    /**
     * Get all visible reviews for public pages.
     * @return List of public DTOs
     */
    List<CustomerReviewCustomerDto> getAllPublicVisibleReviews();

    /**
     * Get visible reviews linked to a specific service.
     * @param serviceId Service ID
     * @return List of public DTOs
     */
    List<CustomerReviewCustomerDto> getPublicReviewsByServiceId(Long serviceId);

    /**
     * Get visible reviews linked to a specific blog.
     * @param blogId Blog ID
     * @return List of public DTOs
     */
    List<CustomerReviewCustomerDto> getPublicReviewsByBlogId(Long blogId);
}