package com.epr.service;


import com.epr.dto.admin.rating.PlatformRatingRequestDto;
import com.epr.dto.admin.rating.PlatformRatingResponseDto;
import com.epr.dto.customer.PlatformRatingDto;

import java.util.List;

public interface PlatformRatingService {

    List<PlatformRatingResponseDto> findAllActiveForAdmin();

    List<PlatformRatingDto> findAllActiveAndVisible(); // Public endpoint

    PlatformRatingResponseDto findById(Long id);

    PlatformRatingResponseDto createRating(PlatformRatingRequestDto dto, Long userId);

    PlatformRatingResponseDto updateRating(Long id, PlatformRatingRequestDto dto, Long userId);

    void softDeleteRating(Long id, Long userId);
}