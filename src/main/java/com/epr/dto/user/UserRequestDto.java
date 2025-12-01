// src/main/java/com/epr/dto/user/UserRequestDto.java
package com.epr.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDto {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String mobile;

    private String profilePicture;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "About me is required")
    private String aboutMe;

    private String department;

    private String displayStatus = "1";

    private String facebook;
    private String linkedin;
    private String twitter;

    private String slug;
    private String metaTitle;
    private String metaKeyword;
    private String metaDescription;

    private int accountStatus = 1;

    private List<Long> roleIds;  // For assigning multiple roles
}