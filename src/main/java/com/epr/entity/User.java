package com.epr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String uuid;

    @NotBlank(message = "Please enter name !!")
    @Column(length = 50, nullable = false)
    private String fullName;

    @Email(message = "Please enter a valid email")
    @NotBlank(message = "Please enter email id !!")
    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @NotBlank(message = "Please enter a strong password !!")
    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 15)
    private String mobile;

    @Column(length = 255)
    private String profilePicture;

    @NotBlank(message = "Please enter job title !!")
    @Column(length = 255)
    private String jobTitle;

    @NotBlank(message = "Please write about user !!")
    @Column(columnDefinition = "TEXT")
    private String aboutMe;

    @Column(length = 100)
    private String department;

    @Column(length = 2, columnDefinition = "varchar(2) default '1'")
    private String displayStatus = "1";

    @Column(length = 100)
    private String addedByUUID;

    @Column(length = 45)
    private String ipAddress;

    private LocalDateTime regDate;
    private LocalDateTime modifyDate;

    private String facebook;
    private String linkedin;
    private String twitter;

    private String slug;
    private String metaTitle;
    @Column(columnDefinition = "TEXT")
    private String metaKeyword;
    @Column(columnDefinition = "TEXT")
    private String metaDescription;

    @Column(columnDefinition = "int default 1 COMMENT '1 active, 2 inactive'")
    private int accountStatus = 1;

    @Column(columnDefinition = "int default 2 COMMENT '1 deleted, 2 not deleted'")
    private int deleteStatus = 2;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();



}