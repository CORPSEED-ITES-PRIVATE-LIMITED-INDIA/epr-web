package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subcategories")
@Getter @Setter
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String uuid;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 200, unique = true, nullable = false)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(length = 2, columnDefinition = "varchar(2) default '1'")
    private Integer displayStatus = 1;  // 1=show, 2=hide

    private String metaTitle;

    @Column(columnDefinition = "TEXT")
    private String metaKeyword;

    @Column(columnDefinition = "TEXT")
    private String metaDescription;

    private LocalDateTime postDate;
    private LocalDateTime modifyDate;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;

    @OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Services> services = new ArrayList<>();
}