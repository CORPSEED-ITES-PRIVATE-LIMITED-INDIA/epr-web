package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter @Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String uuid;

    @Column(length = 100, unique = true, nullable = false)
    private String name;

    @Column(length = 200, unique = true, nullable = false)
    private String slug;

    @Column(length = 255)
    private String icon;

    @Column(name = "sequence_position")
    private Integer sequence = 0;

    @Column(length = 2, columnDefinition = "varchar(2) default '1'")
    private String displayStatus = "1";  // 1=show, 2=hide

    @Column(length = 2, columnDefinition = "varchar(2) default '2'")
    private String showHomeStatus = "2"; // 1=show on home, 2=don't

    // SEO Fields
    private String metaTitle;
    @Column(columnDefinition = "TEXT")
    private String metaKeyword;
    @Column(columnDefinition = "TEXT")
    private String metaDescription;
    @Column(columnDefinition = "TEXT")
    private String searchKeywords;

    private String postDate;
    private String modifyDate;
    private String addedByUUID;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2; // 1=deleted, 2=active

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subcategory> subcategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services = new ArrayList<>();


}