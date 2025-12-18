package com.epr.serviceimpl;


import com.epr.dto.admin.review.CustomerReviewCustomerDto;
import com.epr.dto.admin.review.CustomerReviewRequestDto;
import com.epr.dto.admin.review.CustomerReviewResponseDto;
import com.epr.entity.*;
import com.epr.repository.*;
import com.epr.service.CustomerReviewService;
import com.epr.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerReviewServiceImpl implements CustomerReviewService {

    private final CustomerReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final BlogRepository blogRepository;
    private final DateTimeUtil dateTimeUtil;

    // ====================== ADMIN ======================

    public List<CustomerReviewResponseDto> getAllAdminReviews() {
        return reviewRepository.findAllByDeleteStatus(2, Sort.by("postDate").descending())
                .stream()
                .map(this::toAdminResponseDto)
                .collect(Collectors.toList());
    }

    public CustomerReviewResponseDto createReview(CustomerReviewRequestDto dto, Long userId) {
        validateDto(dto);
        User user = validateUser(userId);

        CustomerReview review = new CustomerReview();
        mapDtoToEntity(dto, review);

        review.setUuid(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
        review.setPostDate(dateTimeUtil.getCurrentUtcTime());
        review.setModifyDate(dateTimeUtil.getCurrentUtcTime());
        review.setAddedByUUID(user.getUuid());

        setRelations(review, dto.getServiceId(), dto.getBlogId());

        CustomerReview saved = reviewRepository.save(review);
        return toAdminResponseDto(saved);
    }

    public CustomerReviewResponseDto updateReview(Long id, CustomerReviewRequestDto dto, Long userId) {
        validateDto(dto);
        validateUser(userId);

        CustomerReview existing = reviewRepository.findById(id)
                .filter(r -> r.getDeleteStatus() == 2)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        mapDtoToEntity(dto, existing);
        existing.setModifyDate(dateTimeUtil.getCurrentUtcTime());
        existing.setModifyByUUID(validateUser(userId).getUuid());

        setRelations(existing, dto.getServiceId(), dto.getBlogId());

        return toAdminResponseDto(reviewRepository.save(existing));
    }

    public void softDeleteReview(Long id, Long userId) {
        validateUser(userId);
        CustomerReview review = reviewRepository.findById(id)
                .filter(r -> r.getDeleteStatus() == 2)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        review.setDeleteStatus(1);
        review.setModifyDate(dateTimeUtil.getCurrentUtcTime());
        reviewRepository.save(review);
    }


    @Override
    public List<CustomerReviewCustomerDto> getPublicFeaturedReviews(int limit) {
        return reviewRepository.findByIsFeaturedAndDisplayStatusAndDeleteStatus(
                        1, 1, 2, Sort.by("postDate").descending())
                .stream()
                .limit(limit)
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerReviewCustomerDto> getAllPublicVisibleReviews() {
        return reviewRepository.findByDisplayStatusAndDeleteStatus(1, 2, Sort.by("postDate").descending())
                .stream()
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }


    private void validateDto(CustomerReviewRequestDto dto) {
        if (dto == null) throw new IllegalArgumentException("Review data is required");
        if (dto.getServiceId() != null && dto.getBlogId() != null) {
            throw new IllegalArgumentException("Review can be linked to either Service OR Blog, not both");
        }
    }

    private User validateUser(Long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found or inactive"));
    }

    private void setRelations(CustomerReview review, Long serviceId, Long blogId) {
        review.setService(null);
        review.setBlog(null);

        if (serviceId != null && serviceId > 0) {
            Services service = serviceRepository.findByIdAndDeleteStatus(serviceId, 2)
                    .orElseThrow(() -> new IllegalArgumentException("Service not found"));
            review.setService(service);
        }
        if (blogId != null && blogId > 0) {
            Blogs blog = blogRepository.findByIdAndDeleteStatus(blogId, 2)
                    .orElseThrow(() -> new IllegalArgumentException("Blog not found"));
            review.setBlog(blog);
        }
    }

    private void mapDtoToEntity(CustomerReviewRequestDto dto, CustomerReview entity) {
        entity.setCustomerName(dto.getCustomerName().trim());
        entity.setCustomerDesignation(dto.getCustomerDesignation());
        entity.setCustomerCompany(dto.getCustomerCompany());
        entity.setCustomerPhoto(dto.getCustomerPhoto());
        entity.setReviewMessage(dto.getReviewMessage().trim());
        entity.setRating(dto.getRating());
        entity.setIsFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : 2);
        entity.setDisplayStatus(dto.getDisplayStatus() != null ? dto.getDisplayStatus() : 1);
    }

    private CustomerReviewResponseDto toAdminResponseDto(CustomerReview r) {
        CustomerReviewResponseDto dto = new CustomerReviewResponseDto();
        dto.setId(r.getId());
        dto.setUuid(r.getUuid());
        dto.setCustomerName(r.getCustomerName());
        dto.setCustomerDesignation(r.getCustomerDesignation());
        dto.setCustomerCompany(r.getCustomerCompany());
        dto.setCustomerPhoto(r.getCustomerPhoto());
        dto.setReviewMessage(r.getReviewMessage());
        dto.setRating(r.getRating());
        dto.setIsFeatured(r.getIsFeatured());
        dto.setDisplayStatus(r.getDisplayStatus());
        dto.setDeleteStatus(r.getDeleteStatus());
        dto.setPostDate(dateTimeUtil.formatDateTimeIst(r.getPostDate()));
        dto.setModifyDate(r.getModifyDate() != null ? dateTimeUtil.formatDateTimeIst(r.getModifyDate()) : null);

        if (r.getService() != null) dto.setServiceTitle(r.getService().getTitle());
        if (r.getBlog() != null) dto.setBlogTitle(r.getBlog().getTitle());

        return dto;
    }

    private CustomerReviewCustomerDto toCustomerDto(CustomerReview r) {
        CustomerReviewCustomerDto dto = new CustomerReviewCustomerDto();
        dto.setCustomerName(r.getCustomerName());
        dto.setCustomerDesignation(r.getCustomerDesignation());
        dto.setCustomerCompany(r.getCustomerCompany());
        dto.setCustomerPhoto(r.getCustomerPhoto());
        dto.setReviewMessage(r.getReviewMessage());
        dto.setRating(r.getRating());
        if (r.getService() != null) dto.setServiceTitle(r.getService().getTitle());
        if (r.getBlog() != null) dto.setBlogTitle(r.getBlog().getTitle());
        return dto;
    }
}