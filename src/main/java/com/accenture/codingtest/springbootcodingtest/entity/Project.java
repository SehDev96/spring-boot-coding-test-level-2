package com.accenture.codingtest.springbootcodingtest.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "app_project")
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "created_date",nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date",nullable = false)
    private LocalDateTime updatedDate;

    public Project() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }
}
