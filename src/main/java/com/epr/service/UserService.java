package com.epr.service;

import com.epr.dto.user.UserRequestDto;
import com.epr.dto.user.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> findAllActiveUsers();

    UserResponseDto findById(Long id);

    UserResponseDto createUser(UserRequestDto dto);

    UserResponseDto updateUser(Long id, UserRequestDto dto);

    void softDeleteUser(Long id);

    List<UserResponseDto> searchUsers(String keyword);


}
