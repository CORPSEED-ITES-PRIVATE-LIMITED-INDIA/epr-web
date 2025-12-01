package com.epr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter @Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String uuid;

    @Column(length = 50, nullable = false, unique = true)
    private String roleName;                    // Super Admin, Marketing, Editor, Sales

    @Column(length = 2, columnDefinition = "varchar(2) default '1'")
    private int displayStatus = 1;

    private LocalDateTime postDate;
    private LocalDateTime modifyDate;

    @Column(columnDefinition = "int default 2")
    private int deleteStatus = 2;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}