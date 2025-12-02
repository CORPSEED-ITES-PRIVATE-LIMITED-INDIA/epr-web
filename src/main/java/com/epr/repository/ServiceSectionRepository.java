// src/main/java/com/epr/repository/ServiceSectionRepository.java
package com.epr.repository;

import com.epr.entity.ServiceSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceSectionRepository extends JpaRepository<ServiceSection, Long> {

    Optional<ServiceSection> findByIdAndDeleteStatus(Long id, int deleteStatus);

    List<ServiceSection> findByServiceIdAndDeleteStatusOrderByDisplayOrderAsc(Long serviceId, int deleteStatus);

    boolean existsByTabNameIgnoreCaseAndServiceId(String tabName, Long serviceId);

    @Query("SELECT COUNT(s) > 0 FROM ServiceSection s WHERE LOWER(s.tabName) = LOWER(:tabName) AND s.service.id = :serviceId AND s.id != :id")
    boolean existsByTabNameIgnoreCaseAndServiceIdAndIdNot(@Param("tabName") String tabName,
                                                          @Param("serviceId") Long serviceId,
                                                          @Param("id") Long id);
}