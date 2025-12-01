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
    private String tabName;        // e.g., "Overview", "Process", "Documents Required"

    @Column(length = 300)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(length = 3)
    private String displayOrder = "1";

    @Column(length = 2)
    private String displayStatus = "1";

    private String postDate;
    private String modifyDate;
    private String addedByUUID;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<ServiceSectionCard> cards = new ArrayList<>();
}