// src/main/java/com/epr/service/RoleService.java
package com.epr.service;

import com.epr.dto.admin.role.RoleRequestDto;
import com.epr.dto.admin.role.RoleResponseDto;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> findAllActiveRoles();
    RoleResponseDto findById(Long id);
    RoleResponseDto createRole(RoleRequestDto dto);
    RoleResponseDto updateRole(Long id, RoleRequestDto dto);
    void softDeleteRole(Long id);
}