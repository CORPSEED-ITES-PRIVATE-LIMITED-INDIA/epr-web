// src/main/java/com/epr/repository/ServiceRepository.java
package com.epr.repository;

import com.epr.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

    // All active services (not soft-deleted), ordered by sequence
    @Query("SELECT s FROM Services s WHERE s.deleteStatus = 2 ORDER BY s.sequence ASC")
    List<Services> findAllActiveServices();

    // Find active service by ID
    @Query("SELECT s FROM Services s WHERE s.id = :id AND s.deleteStatus = 2")
    Optional<Services> findActiveById(@Param("id") Long id);

    // Slug uniqueness check (exclude current record during update)
    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);

    // Title uniqueness
    boolean existsByTitleIgnoreCase(String title);
    boolean existsByTitleIgnoreCaseAndIdNot(String title, Long id);

    // Find by UUID
    Optional<Services> findByUuid(String uuid);

    // Services to show on homepage (display + showHome + active)
    @Query("SELECT s FROM Services s WHERE s.displayStatus = 1 AND s.showHomeStatus = 1 AND s.deleteStatus = 2 ORDER BY s.sequence ASC")
    List<Services> findHomePageServices();

    // Search in title or short description (case-insensitive)
    @Query("SELECT s FROM Services s WHERE s.deleteStatus = 2 " +
            "AND (LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Services> searchActiveServices(@Param("keyword") String keyword);
}