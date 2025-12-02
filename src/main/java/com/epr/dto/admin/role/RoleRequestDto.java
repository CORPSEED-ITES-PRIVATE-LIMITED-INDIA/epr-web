// src/main/java/com/epr/dto/role/RoleRequestDto.java
package com.epr.dto.admin.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequestDto {

    @NotBlank(message = "Role name is required")
    private String roleName;

    private int displayStatus = 1;
}