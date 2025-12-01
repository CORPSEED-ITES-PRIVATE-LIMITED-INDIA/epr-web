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

    Optional<Subcategory> findByIdAndDeleteStatus(Long id, int deleteStatus);

    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlugIgnoreCase(String slug);

    @Query("SELECT COUNT(s) > 0 FROM Subcategory s WHERE LOWER(s.name) = LOWER(:name) AND s.id != :id")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Query("SELECT COUNT(s) > 0 FROM Subcategory s WHERE LOWER(s.slug) = LOWER(:slug) AND s.id != :id")
    boolean existsBySlugIgnoreCaseAndIdNot(@Param("slug") String slug, @Param("id") Long id);


}