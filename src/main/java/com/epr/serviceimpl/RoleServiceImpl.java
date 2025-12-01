package com.epr.serviceimpl;

import com.epr.dto.role.RoleRequestDto;
import com.epr.dto.role.RoleResponseDto;
import com.epr.entity.Role;
import com.epr.repository.RoleRepository;
import com.epr.service.RoleService;
import com.epr.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.epr.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DateTimeUtil dateTimeUtil;


    public List<RoleResponseDto> findAllActiveRoles() {
        return roleRepository.findAllActiveRoles()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDto findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid role ID provided");
        }

        Role role = roleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        return toResponseDto(role);
    }
    public RoleResponseDto createRole(RoleRequestDto dto) {
        if (existsByRoleName(dto.getRoleName(), null)) {
            throw new IllegalArgumentException("Role name already exists");
        }

        Role role = new Role();
        role.setRoleName(dto.getRoleName());
        role.setDisplayStatus(dto.getDisplayStatus() != 0 ? dto.getDisplayStatus() : 1);
        role.setUuid(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
        role.setPostDate(dateTimeUtil.getCurrentUtcTime());

        Role saved = roleRepository.save(role);
        return toResponseDto(saved);
    }

    public RoleResponseDto updateRole(Long id, RoleRequestDto dto) {
        Role existing = roleRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        if (existsByRoleName(dto.getRoleName(), id)) {
            throw new IllegalArgumentException("Role name already exists");
        }

        existing.setRoleName(dto.getRoleName());
        existing.setDisplayStatus(dto.getDisplayStatus() != 0 ? dto.getDisplayStatus() : 1);
        existing.setModifyDate(dateTimeUtil.getCurrentUtcTime());

        Role updated = roleRepository.save(existing);
        return toResponseDto(updated);
    }

    public void softDeleteRole(Long id) {
        Role role = roleRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        if (role.getUsers() != null && !role.getUsers().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete role assigned to users");
        }

        role.setDeleteStatus(1);
        role.setModifyDate(dateTimeUtil.getCurrentUtcTime());
        roleRepository.save(role);
    }

    public List<RoleResponseDto> searchRoles(String keyword) {
        return roleRepository.searchByRoleName(keyword)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public boolean existsByRoleName(String roleName, Long excludeId) {
        return roleRepository.existsByRoleNameIgnoreCaseAndNotId(roleName, excludeId);
    }


    private RoleResponseDto toResponseDto(Role role) {
        RoleResponseDto dto = new RoleResponseDto();
        dto.setId(role.getId());
        dto.setUuid(role.getUuid());
        dto.setRoleName(role.getRoleName());
        dto.setDisplayStatus(role.getDisplayStatus());
        dto.setPostDate(role.getPostDate());
        dto.setModifyDate(role.getModifyDate());
        return dto;
    }
}