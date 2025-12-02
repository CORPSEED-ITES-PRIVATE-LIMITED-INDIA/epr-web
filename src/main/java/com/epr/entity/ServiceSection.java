// src/main/java/com/epr/entity/ServiceSection.java
package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_sections")
@Getter @Setter
public class ServiceSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String uuid;

    @Column(length = 150, nullable = false)
    private String tabName;

    @Column(length = 300)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(length = 3, columnDefinition = "varchar(3) default '1'")
    private String displayOrder = "1";

    @Column(columnDefinition = "int default 1")
    private Integer displayStatus = 1; // 1 = show, 2 = hide

    private String postDate;
    private String modifyDate;
    private String addedByUUID;

    // Standardize deleteStatus: 2 = active, 1 = deleted (same as all your other entities)
    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Services service;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceSectionCard> cards = new ArrayList<>();
}