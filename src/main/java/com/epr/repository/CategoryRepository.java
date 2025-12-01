// src/main/java/com/epr/repository/CategoryRepository.java
package com.epr.repository;

import com.epr.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    Optional<Category> findByIdAndDeleteStatus(Long id, int deleteStatus);

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlugIgnoreCase(String slug);

    // For update: Check uniqueness excluding current ID
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.name) = LOWER(:name) AND c.id != :id")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.slug) = LOWER(:slug) AND c.id != :id")
    boolean existsBySlugIgnoreCaseAndIdNot(@Param("slug") String slug, @Param("id") Long id);

}