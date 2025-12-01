package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "services")
@Getter @Setter
public class Service {

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
    private String processingTime;
    private String validity;
    private String priceStartsFrom;
    private String legalGuideLink;

    // SEO
    private String metaTitle;
    @Column(columnDefinition = "TEXT")
    private String metaKeyword;
    @Column(columnDefinition = "TEXT")
    private String metaDescription;

    @Column(columnDefinition = "LONGTEXT")
    private String faqSchema;

    @Column(length = 2, columnDefinition = "varchar(2) default '1'")
    private String displayStatus = "1";

    @Column(length = 2, columnDefinition = "varchar(2) default '2'")
    private String showHomeStatus = "2";

    @Column(name = "sequence_position")
    private Integer sequence = 0;

    private String postDate;
    private String modifyDate;
    private String addedByUUID;

    private Long visited = 0L;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    // Dynamic Tabs (Replaces ServiceDetails + ServiceCardList)
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<ServiceSection> sections = new ArrayList<>();

    // FAQs, Packages, Documents, etc.
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceFaq> faqs = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceDocument> documents = new ArrayList<>();
}