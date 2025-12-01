// src/main/java/com/epr/repository/SubcategoryRepository.java
package com.epr.repository;

import com.epr.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    // Active subcategories (not deleted)
    List<Subcategory> findByDeleteStatusOrderBySequenceAsc(int deleteStatus);

    // Find active subcategory by ID
    Optional<Subcategory> findByIdAndDeleteStatus(Long id, int deleteStatus);

    // Uniqueness checks
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlugIgnoreCase(String slug);

    // For update: exclude current record
    @Query("SELECT COUNT(s) > 0 FROM Subcategory s WHERE LOWER(s.name) = LOWER(:name) AND s.id != :id")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Query("SELECT COUNT(s) > 0 FROM Subcategory s WHERE LOWER(s.slug) = LOWER(:slug) AND s.id != :id")
    boolean existsBySlugIgnoreCaseAndIdNot(@Param("slug") String slug, @Param("id") Long id);

    // Optional: Get active subcategories under a specific category
    List<Subcategory> findByCategoryIdAndDeleteStatusOrderBySequenceAsc(Long categoryId, int deleteStatus);
}