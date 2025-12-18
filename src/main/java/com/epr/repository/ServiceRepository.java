// src/main/java/com/epr/repository/ServiceRepository.java

package com.epr.repository;

import com.epr.entity.Services;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

    @Query("SELECT s FROM Services s WHERE s.id = :id AND s.deleteStatus = 2")
    Optional<Services> findActiveById(@Param("id") Long id);

    @Query("SELECT s FROM Services s WHERE s.deleteStatus = 2 ORDER BY s.postDate DESC")
    List<Services> findAllActiveServices();

    @Query("SELECT s FROM Services s WHERE s.deleteStatus = 2 " +
            "AND (LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "ORDER BY s.postDate DESC")
    List<Services> searchActiveServices(@Param("keyword") String keyword);

    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);
    boolean existsByTitleIgnoreCase(String title);
    boolean existsByTitleIgnoreCaseAndIdNot(String title, Long id);

    @Query("SELECT s FROM Services s WHERE s.id IN :ids AND s.deleteStatus = :deleteStatus")
    List<Services> findAllByIdInAndDeleteStatus(@Param("ids") List<Long> ids, @Param("deleteStatus") int deleteStatus);

    Optional<Services> findBySlugIgnoreCaseAndDeleteStatusAndDisplayStatus(
            String slug, int deleteStatus, int displayStatus);

    @Query("SELECT s FROM Services s WHERE s.category.id = :categoryId " +
            "AND s.deleteStatus = :deleteStatus AND s.displayStatus = :displayStatus")
    List<Services> findByCategoryIdAndDeleteStatusAndDisplayStatus(
            @Param("categoryId") Long categoryId,
            @Param("deleteStatus") int deleteStatus,
            @Param("displayStatus") int displayStatus);

    @Query("SELECT s FROM Services s WHERE s.showHomeStatus = :showHome " +
            "AND s.deleteStatus = :deleteStatus AND s.displayStatus = :displayStatus " +
            "ORDER BY s.postDate DESC")
    List<Services> findByShowHomeStatusAndDeleteStatusAndDisplayStatus(
            @Param("showHome") int showHomeStatus,
            @Param("deleteStatus") int deleteStatus,
            @Param("displayStatus") int displayStatus);

    @Query("SELECT s FROM Services s WHERE s.deleteStatus = 2 AND s.displayStatus = 1 " +
            "AND (LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "ORDER BY s.postDate DESC")
    List<Services> searchActivePublicServices(@Param("keyword") String keyword);

    @Query("SELECT s FROM Services s WHERE s.subcategory.id = :subcategoryId " +
            "AND s.deleteStatus = :deleteStatus AND s.displayStatus = :displayStatus " +
            "ORDER BY s.postDate DESC")
    List<Services> findBySubcategoryIdAndDeleteStatusAndDisplayStatus(
            @Param("subcategoryId") Long subcategoryId,
            @Param("deleteStatus") int deleteStatus,
            @Param("displayStatus") int displayStatus);

    @Query("SELECT s FROM Services s " +
            "WHERE s.deleteStatus = 2 AND s.displayStatus = 1 " +
            "ORDER BY s.postDate DESC")
    List<Services> findTopNActiveAndVisibleServices(int limit); // Note: @Param("limit") not needed for limit in JPQL

    /* ==================== FOOTER QUERY ==================== */

    /**
     * Find services to display in footer:
     * - showInFooter = 1
     * - deleteStatus = 2
     * - displayStatus = 1
     * - Ordered by footerOrder
     */
    @Query("SELECT s FROM Services s " +
            "WHERE s.showInFooter = :showInFooter " +
            "AND s.deleteStatus = :deleteStatus " +
            "AND s.displayStatus = :displayStatus")
    List<Services> findByShowInFooterAndDeleteStatusAndDisplayStatus(
            @Param("showInFooter") int showInFooter,
            @Param("deleteStatus") int deleteStatus,
            @Param("displayStatus") int displayStatus,
            Sort sort);
}