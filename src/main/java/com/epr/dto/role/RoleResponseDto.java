// src/main/java/com/epr/dto/role/RoleResponseDto.java
package com.epr.dto.role;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleResponseDto {
    private Long id;
    private String uuid;
    private String roleName;
    private int displayStatus;
    private LocalDateTime postDate;
    private LocalDateTime modifyDate;
}