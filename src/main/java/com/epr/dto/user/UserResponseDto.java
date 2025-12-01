// src/main/java/com/epr/dto/user/UserResponseDto.java
package com.epr.dto.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponseDto {
    private Long id;
    private String uuid;
    private String fullName;
    private String email;
    private String mobile;
    private String profilePicture;
    private String jobTitle;
    private String aboutMe;
    private String department;
    private String displayStatus;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
    private String facebook;
    private String linkedin;
    private String twitter;
    private String slug;
    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;
    private int accountStatus;
    private List<String> roleNames;  // Safe - only names, no full roles
}