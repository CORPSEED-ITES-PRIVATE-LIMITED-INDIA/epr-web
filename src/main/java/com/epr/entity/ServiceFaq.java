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

    private String question;
    @Column(columnDefinition = "LONGTEXT")
    private String answer;

    private Integer displayOrder = 0;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services service;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;
}