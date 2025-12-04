package com.epr.serviceimpl;



import com.epr.dto.admin.rating.PlatformRatingRequestDto;
import com.epr.dto.admin.rating.PlatformRatingResponseDto;
import com.epr.dto.customer.PlatformRatingDto;
import com.epr.entity.PlatformRating;
import com.epr.entity.User;
import com.epr.repository.PlatformRatingRepository;
import com.epr.repository.UserRepository;
import com.epr.service.PlatformRatingService;
import com.epr.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlatformRatingServiceImpl implements PlatformRatingService {

    private static final Logger log = LoggerFactory.getLogger(PlatformRatingServiceImpl.class);

    private final PlatformRatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final DateTimeUtil dateTimeUtil;

    private User validateAndGetActiveUser(Long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found or inactive"));
    }

    @Override
    public List<PlatformRatingResponseDto> findAllActiveForAdmin() {
        return ratingRepository.findAllByDeleteStatusOrderByRatingDesc(2)
                .stream()
                .map(this::toAdminResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlatformRatingDto> findAllActiveAndVisible() {
        return ratingRepository.findAllActiveAndVisible()
                .stream()
                .map(this::toCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlatformRatingResponseDto findById(Long id) {
        PlatformRating rating = ratingRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with id: " + id));
        return toAdminResponseDto(rating);
    }

    @Override
    public PlatformRatingResponseDto createRating(PlatformRatingRequestDto dto, Long userId) {
        validateDto(dto);
        User user = validateAndGetActiveUser(userId);

        if (ratingRepository.existsByPlatformIgnoreCaseAndIdNot(dto.getPlatform().trim(), null)) {
            throw new IllegalArgumentException("Platform '" + dto.getPlatform() + "' already exists");
        }

        PlatformRating rating = new PlatformRating();
        mapDtoToEntity(dto, rating);
        rating.setAddedByUUID(user.getUuid());

        PlatformRating saved = ratingRepository.save(rating);
        log.info("Platform rating created: {} by userId={}", dto.getPlatform(), userId);
        return toAdminResponseDto(saved);
    }

    @Override
    public PlatformRatingResponseDto updateRating(Long id, PlatformRatingRequestDto dto, Long userId) {
        validateDto(dto);
        User user = validateAndGetActiveUser(userId);

        PlatformRating existing = ratingRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));

        String newPlatformKey = dto.getPlatform().trim().toLowerCase();
        if (!existing.getPlatform().equalsIgnoreCase(newPlatformKey) &&
                ratingRepository.existsByPlatformIgnoreCaseAndIdNot(newPlatformKey, id)) {
            throw new IllegalArgumentException("Platform '" + dto.getPlatform() + "' already exists");
        }

        mapDtoToEntity(dto, existing);
        existing.setUpdatedByUUID(user.getUuid());
        existing.setUpdatedAt(dateTimeUtil.getCurrentUtcTime());

        PlatformRating updated = ratingRepository.save(existing);
        log.info("Platform rating updated: {} (ID: {}) by userId={}", dto.getPlatform(), id, userId);
        return toAdminResponseDto(updated);
    }

    @Override
    public void softDeleteRating(Long id, Long userId) {
        validateAndGetActiveUser(userId);

        PlatformRating rating = ratingRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));

        rating.setDeleteStatus(1);
        ratingRepository.save(rating);
        log.info("Platform rating soft deleted: ID={} by userId={}", id, userId);
    }

    // === Helpers ===
    private void validateDto(PlatformRatingRequestDto dto) {
        if (dto == null) throw new IllegalArgumentException("Rating data is required");
        if (dto.getPlatform() == null || dto.getPlatform().trim().isEmpty())
            throw new IllegalArgumentException("Platform key is required");
        if (dto.getPlatformDisplayName() == null || dto.getPlatformDisplayName().trim().isEmpty())
            throw new IllegalArgumentException("Display name is required");
        if (dto.getRating() == null || dto.getRating() < 0 || dto.getRating() > 5)
            throw new IllegalArgumentException("Valid rating (0.0 - 5.0) is required");
    }

    private void mapDtoToEntity(PlatformRatingRequestDto dto, PlatformRating entity) {
        entity.setPlatform(dto.getPlatform().trim().toLowerCase());
        entity.setPlatformDisplayName(dto.getPlatformDisplayName().trim());
        entity.setRating(dto.getRating());
        entity.setTotalReviews(dto.getTotalReviews());
        entity.setReviewUrl(dto.getReviewUrl());
        entity.setIconUrl(dto.getIconUrl());
        entity.setDisplayStatus(dto.getDisplayStatus() != null ? dto.getDisplayStatus() : 1);
    }

    private PlatformRatingResponseDto toAdminResponseDto(PlatformRating r) {
        PlatformRatingResponseDto dto = new PlatformRatingResponseDto();
        dto.setId(r.getId());
        dto.setPlatform(r.getPlatform());
        dto.setPlatformDisplayName(r.getPlatformDisplayName());
        dto.setRating(r.getRating());
        dto.setTotalReviews(r.getTotalReviews());
        dto.setReviewUrl(r.getReviewUrl());
        dto.setIconUrl(r.getIconUrl());
        dto.setDisplayStatus(r.getDisplayStatus());
        dto.setDeleteStatus(r.getDeleteStatus());
        dto.setCreatedAt(dateTimeUtil.formatDateTimeIst(r.getCreatedAt()));
        dto.setUpdatedAt(r.getUpdatedAt() != null ? dateTimeUtil.formatDateTimeIst(r.getUpdatedAt()) : null);
        return dto;
    }

    private PlatformRatingDto toCustomerDto(PlatformRating r) {
        PlatformRatingDto dto = new PlatformRatingDto();
        dto.setPlatform(r.getPlatform());
        dto.setPlatformDisplayName(r.getPlatformDisplayName());
        dto.setRating(r.getRating());
        dto.setTotalReviews(r.getTotalReviews());
        dto.setReviewUrl(r.getReviewUrl());
        dto.setIconUrl(r.getIconUrl());
        return dto;
    }
}