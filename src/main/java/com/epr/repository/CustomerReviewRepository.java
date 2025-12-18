package com.epr.repository;

import com.epr.entity.CustomerReview;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerReviewRepository extends JpaRepository<CustomerReview, Long> {

    List<CustomerReview> findAllByDeleteStatus(int deleteStatus, Sort sort);

    List<CustomerReview> findByDisplayStatusAndDeleteStatus(int displayStatus, int deleteStatus, Sort sort);

    List<CustomerReview> findByIsFeaturedAndDisplayStatusAndDeleteStatus(
            int isFeatured, int displayStatus, int deleteStatus, Sort sort);

}