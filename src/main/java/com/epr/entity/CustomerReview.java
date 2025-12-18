package com.epr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "customer_reviews",
        indexes = {
                @Index(name = "idx_reviews_display_delete", columnList = "displayStatus, deleteStatus"),
                @Index(name = "idx_reviews_rating", columnList = "rating DESC"),
                @Index(name = "idx_reviews_featured", columnList = "isFeatured, displayStatus, deleteStatus"),
                @Index(name = "idx_reviews_postdate", columnList = "postDate DESC")
        }
)
@Getter
@Setter
public class CustomerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false, updatable = false)
    private String uuid;

    @NotBlank(message = "Customer name is required")
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String customerName;

    @Size(max = 150)
    @Column(length = 150)
    private String customerDesignation; // e.g., CEO, Founder, Manager

    @Column(length = 255)
    private String customerCompany; // Optional company name

    @Column(length = 500)
    private String customerPhoto; // URL to customer's photo/avatar (optional)

    @NotBlank(message = "Review message is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String reviewMessage;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating = 5; // 1 to 5 stars

    // Optional: Link review to a specific service (if review is service-specific)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Services service;

    // Optional: Link to a blog (if review is about a blog post)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blogs blog;

    // For featuring on homepage, testimonials section, etc.
    @Column(nullable = false, columnDefinition = "tinyint default 2")
    private int isFeatured = 2; // 1 = Featured, 2 = Normal

    // Visibility control
    @Column(nullable = false, columnDefinition = "tinyint default 1")
    private int displayStatus = 1; // 1 = Visible, 2 = Hidden

    @Column(nullable = false, columnDefinition = "tinyint default 2")
    private int deleteStatus = 2; // 1 = Soft deleted, 2 = Active

    @CreationTimestamp
    @Column(name = "post_date", nullable = false, updatable = false)
    private LocalDateTime postDate;

    @UpdateTimestamp
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column(length = 100)
    private String addedByUUID; // Admin who added the review (if manually added)

    @Column(length = 100)
    private String modifyByUUID;

    // Optional: If review comes from verified customer order/user
    @Column(length = 100)
    private String customerUuid; // Reference to actual user if logged in

    @Column(length = 255)
    private String customerEmail; // Optional, can be used for verification
}