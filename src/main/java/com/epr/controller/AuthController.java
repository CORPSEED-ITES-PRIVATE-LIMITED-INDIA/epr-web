package com.epr.controller;

import com.epr.dto.login.LoginRequestDto;
import com.epr.dto.user.UserResponseDto;
import com.epr.entity.User;
import com.epr.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequest,
                                   HttpServletRequest request) {

        try {
            // Perform authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Set authentication in Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Create or update session
            HttpSession session = request.getSession(true); // true = create if not exists
            session.setMaxInactiveInterval(24 * 60 * 60); // 24 hours

            // Get the authenticated user (Spring Security UserDetails)
            org.springframework.security.core.userdetails.User springUser =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            // Load full User entity to return rich data
            User userEntity = userService.findByEmail(springUser.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found after login"));

            UserResponseDto responseDto = convertToResponseDto(userEntity);

            // Optional: return session ID in header
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", responseDto);

            return ResponseEntity.ok()
                    .header("X-Session-Id", session.getId())
                    .body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    private UserResponseDto convertToResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUuid(user.getUuid());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setJobTitle(user.getJobTitle());
        dto.setAboutMe(user.getAboutMe());
        dto.setDepartment(user.getDepartment());
        dto.setDisplayStatus(user.getDisplayStatus());
        dto.setRegDate(user.getRegDate());
        dto.setModifyDate(user.getModifyDate());
        dto.setFacebook(user.getFacebook());
        dto.setLinkedin(user.getLinkedin());
        dto.setTwitter(user.getTwitter());
        dto.setSlug(user.getSlug());
        dto.setMetaTitle(user.getMetaTitle());
        dto.setMetaKeyword(user.getMetaKeyword());
        dto.setMetaDescription(user.getMetaDescription());
        dto.setRoleNames(user.getRoles().stream()
                .map(role -> role.getRoleName())
                .toList());
        return dto;
    }
}