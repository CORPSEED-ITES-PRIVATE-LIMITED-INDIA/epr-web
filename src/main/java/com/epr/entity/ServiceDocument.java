package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_documents")
@Getter @Setter
public class ServiceDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String uuid;

    @Column(length = 200, nullable = false)
    private String documentName;        // e.g., "Aadhaar Card", "Company PAN", "Authorization Letter"

    @Column(length = 500)
    private String description;         // Optional: "Front & Back both sides required"

    @Column(length = 500)
    private String sampleLink;          // URL to sample format (PDF/Image)

    @Column(length = 500)
    private String downloadLink;        // Direct download link (optional)

    @Column(length = 10)
    private String format;              // e.g., PDF, JPG, PNG, Word

    private Boolean isMandatory = true; // true = Required, false = Optional

    @Column(length = 3)
    private Integer displayOrder = 0;   // For sorting on frontend

    @Column(length = 2, columnDefinition = "varchar(2) default '1'")
    private String displayStatus = "1"; // 1=show, 2=hide

    private String postDate;
    private String modifyDate;
    private String addedByUUID;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;       // 1=deleted, 2=active

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    // Optional: If you want to group documents under a tab/section later
    // @ManyToOne
    // private ServiceSection section;
}