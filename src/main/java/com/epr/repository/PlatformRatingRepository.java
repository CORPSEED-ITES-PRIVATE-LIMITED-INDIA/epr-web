// src/main/java/com/epr/repository/PlatformRatingRepository.java
package com.epr.repository;

import com.epr.entity.PlatformRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlatformRatingRepository extends JpaRepository<PlatformRating, Long> {

    @Query("SELECT pr FROM PlatformRating pr WHERE pr.deleteStatus = 2 AND pr.displayStatus = 1 ORDER BY pr.rating DESC, pr.totalReviews DESC")
    List<PlatformRating> findAllActiveAndVisible();

    @Query("SELECT pr FROM PlatformRating pr WHERE pr.id = :id AND pr.deleteStatus = 2")
    Optional<PlatformRating> findActiveById(@Param("id") Long id);

    List<PlatformRating> findAllByDeleteStatusOrderByRatingDesc(int deleteStatus);

    boolean existsByPlatformIgnoreCaseAndIdNot(String platform, Long id);
}