// src/main/java/com/epr/service/UserService.java
package com.epr.service;

import com.epr.dto.admin.user.UserRequestDto;
import com.epr.dto.admin.user.UserResponseDto;
import com.epr.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDto> findAllActiveUsers();

    UserResponseDto findById(Long id);

    UserResponseDto createUser(UserRequestDto dto);

    UserResponseDto updateUser(Long id, UserRequestDto dto);

    void softDeleteUser(Long id);

    List<UserResponseDto> searchUsers(String keyword);

    Optional<User> findByEmail(String email);
}