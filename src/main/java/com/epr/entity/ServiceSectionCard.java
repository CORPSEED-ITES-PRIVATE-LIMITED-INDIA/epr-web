package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_section_cards")
@Getter @Setter
public class ServiceSectionCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String uuid;

    @Column(length = 300)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String icon;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private ServiceSection section;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;
}