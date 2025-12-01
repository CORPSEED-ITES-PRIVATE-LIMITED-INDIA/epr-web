package com.epr.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "enquiries")
@Getter
@Setter
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String uuid;

    @Column(length = 250)
    private String type;                    // e.g., "General", "Service Enquiry", "Callback"

    @Column(columnDefinition = "TEXT")
    private String message;

    @NotBlank(message = "Please enter your name !!")
    @Column(length = 255, nullable = false)
    private String name;

    @Email(message = "Please enter a valid email")
    @NotBlank(message = "Please enter email !!")
    @Column(length = 255, nullable = false)
    private String email;

    @NotBlank(message = "Please enter mobile number !!")
    @Size(min = 10, max = 15, message = "Mobile must be 10-15 digits")
    @Column(length = 50, nullable = false)
    private String mobile;

    @NotBlank(message = "Please enter your city !!")
    @Column(length = 250, nullable = false)
    private String city;

    @Column(length = 100)
    private String categoryId;

    @Column(length = 100)
    private String serviceId;

    @Column(length = 100)
    private String industryId;

    @Column(columnDefinition = "TINYTEXT")
    private String url;

    @Column(length = 45)
    private String ipAddress;

    // UTM Tracking
    @Column(length = 100)
    private String utmSource;
    @Column(length = 100)
    private String utmMedium;
    @Column(length = 100)
    private String utmCampaign;
    @Column(length = 100)
    private String utmTerm;
    @Column(length = 100)
    private String utmContent;

    @Column(length = 2, columnDefinition = "varchar(2) default '1'")
    private String displayStatus = "1";

    @Column(columnDefinition = "int default 2 COMMENT '1=deleted, 2=active'")
    private int deleteStatus = 2;

    @Column(columnDefinition = "int default 2 COMMENT '1=Synced, 2=Pending'")
    private int erpStatus = 2;


}