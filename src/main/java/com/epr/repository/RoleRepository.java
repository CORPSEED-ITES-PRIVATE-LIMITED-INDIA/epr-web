package com.epr.repository;

import com.epr.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // FIXED: '1' → 1   and   2 → 2 (no quotes!)
    @Query("SELECT r FROM Role r WHERE r.deleteStatus = 2 AND r.displayStatus = 1 ORDER BY r.roleName ASC")
    List<Role> findAllActiveRoles();

    @Query("SELECT r FROM Role r WHERE r.id = :id AND r.deleteStatus = 2")
    Optional<Role> findActiveById(@Param("id") Long id);
        @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Role r WHERE r.roleName = :roleName AND r.deleteStatus = 2 AND (:id IS NULL OR r.id != :id)")
    boolean existsByRoleNameIgnoreCaseAndNotId(@Param("roleName") String roleName, @Param("id") Long id);

    @Query("SELECT r FROM Role r WHERE r.roleName LIKE %:keyword% AND r.deleteStatus = 2")
    List<Role> searchByRoleName(@Param("keyword") String keyword);
}