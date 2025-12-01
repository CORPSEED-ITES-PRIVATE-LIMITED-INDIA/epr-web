package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "enquiry_blocker")
@Getter
@Setter
public class EnquiryBlocker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String mobile;

    @Column(length = 255)
    private String email;

    private LocalDateTime blockedAt;

    @Column(length = 500)
    private String reason;                  // Optional: "Spam", "Fake Lead", "Abusive"

    @Column(length = 100)
    private String blockedByUUID;           // Admin who blocked

    @PrePersist
    protected void onCreate() {
        this.blockedAt = LocalDateTime.now();
    }
}