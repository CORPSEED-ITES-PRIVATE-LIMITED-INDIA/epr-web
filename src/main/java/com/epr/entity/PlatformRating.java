package com.epr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "platform_ratings",
        indexes = {
                @Index(name = "idx_platform_ratings_active", columnList = "displayStatus"),
                @Index(name = "idx_platform_ratings_platform", columnList = "platform")
        }
)
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class PlatformRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Platform name is required")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String platform; // e.g., Google, Trustpilot, Glassdoor, Facebook, Instagram

    @NotBlank(message = "Platform display name is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String platformDisplayName; // e.g., "Google Reviews", "Trustpilot"

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    @Column(nullable = false, precision = 3)
    private Double rating; // e.g., 4.8

    @Min(0)
    private Integer totalReviews; // e.g., 1250

    @Size(max = 255)
    private String reviewUrl; // Link to the review page (optional but recommended)

    @Size(max = 255)
    private String iconUrl; // Optional: logo of platform (Google, Trustpilot, etc.)

    @Column(nullable = false, columnDefinition = "tinyint default 1")
    private int displayStatus = 1; // 1 = Show, 2 = Hide

    @Column(nullable = false, columnDefinition = "tinyint default 2")
    private int deleteStatus = 2; // 2 = Active, 1 = Deleted (soft delete)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Optional: who added/updated
    @Column(length = 100)
    private String addedByUUID;

    @Column(length = 100)
    private String updatedByUUID;

    // Helper method
    public boolean isActive() {
        return deleteStatus == 2 && displayStatus == 1;
    }
}