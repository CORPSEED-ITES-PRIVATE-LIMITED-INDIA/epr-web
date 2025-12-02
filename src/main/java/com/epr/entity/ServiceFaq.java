package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "service_faqs")
@Getter @Setter
public class ServiceFaq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String uuid;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(columnDefinition = "LONGTEXT")
    private String answer;

    @Column(columnDefinition = "int default 0")
    private Integer displayOrder = 0;

    @Column(columnDefinition = "int default 1")
    private Integer displayStatus = 1; // 1=show, 2=hide

    // Audit fields
    private String addedByUUID;
    private String modifyByUUID;
    private String postDate;
    private String modifyDate;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Services service;
}