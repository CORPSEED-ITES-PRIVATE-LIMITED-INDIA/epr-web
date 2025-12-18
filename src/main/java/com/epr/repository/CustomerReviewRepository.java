package com.epr.repository;

import com.epr.entity.CustomerReview;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerReviewRepository extends JpaRepository<CustomerReview, Long> {

    List<CustomerReview> findAllByDeleteStatus(int deleteStatus, Sort sort);

    List<CustomerReview> findByDisplayStatusAndDeleteStatus(int displayStatus, int deleteStatus, Sort sort);

    List<CustomerReview> findByIsFeaturedAndDisplayStatusAndDeleteStatus(
            int isFeatured, int displayStatus, int deleteStatus, Sort sort);

    @Query("SELECT cr FROM CustomerReview cr " +
            "WHERE cr.service.id = :serviceId " +
            "AND cr.displayStatus = 1 AND cr.deleteStatus = 2")
    List<CustomerReview> findPublicByServiceId(@Param("serviceId") Long serviceId);

    @Query("SELECT cr FROM CustomerReview cr " +
            "WHERE cr.blog.id = :blogId " +
            "AND cr.displayStatus = 1 AND cr.deleteStatus = 2")
    List<CustomerReview> findPublicByBlogId(@Param("blogId") Long blogId);
}