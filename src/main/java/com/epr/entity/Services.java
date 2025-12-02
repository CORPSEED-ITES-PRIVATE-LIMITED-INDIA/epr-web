// src/main/java/com/epr/entity/Services.java
package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "services")
@Getter @Setter
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String uuid;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(unique = true, length = 250, nullable = false)
    private String slug;

    @Column(length = 500)
    private String shortDescription;

    @Column(columnDefinition = "LONGTEXT")
    private String fullDescription;

    private String bannerImage;
    private String thumbnail;
    private String videoUrl;
    // SEO
    private String metaTitle;
    @Column(columnDefinition = "TEXT")
    private String metaKeyword;
    @Column(columnDefinition = "TEXT")
    private String metaDescription;

    @Column(length = 2, columnDefinition = "int default 1")
    private int displayStatus = 1;        // 1 = show, 2 = hide

    @Column(length = 2, columnDefinition = "int default 2")
    private int showHomeStatus = 2;       // 1 = show on home, 2 = hide

    private LocalDateTime postDate;
    private LocalDateTime modifyDate;
    private String addedByUUID;
    private String modifyByUUID;

    private Long visited = 0L;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;         // 1 = deleted, 2 = active

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<ServiceSection> sections = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceFaq> faqs = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceDocument> documents = new ArrayList<>();
}