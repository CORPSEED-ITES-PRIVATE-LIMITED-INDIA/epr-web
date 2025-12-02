// src/main/java/com/epr/repository/ServiceFaqRepository.java
package com.epr.repository;

import com.epr.entity.ServiceFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceFaqRepository extends JpaRepository<ServiceFaq, Long> {

    Optional<ServiceFaq> findByIdAndDeleteStatus(Long id, int deleteStatus);

    List<ServiceFaq> findByServiceIdAndDeleteStatusOrderByDisplayOrderAsc(Long serviceId, int deleteStatus);

    boolean existsByQuestionIgnoreCaseAndServiceId(String question, Long serviceId);

    @Query("SELECT COUNT(f) > 0 FROM ServiceFaq f WHERE LOWER(f.question) = LOWER(:question) AND f.service.id = :serviceId AND f.id != :id")
    boolean existsByQuestionIgnoreCaseAndServiceIdAndIdNot(
            @Param("question") String question,
            @Param("serviceId") Long serviceId,
            @Param("id") Long id);
}