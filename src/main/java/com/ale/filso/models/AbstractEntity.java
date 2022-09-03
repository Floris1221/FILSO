package com.ale.filso.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private boolean isActive = true;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP default CURRENT_DATE")
    private LocalDateTime createdOn;

    @NotNull
    @Column(columnDefinition = "varchar(255) default current_user")
    private String createdBy;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP default CURRENT_DATE")
    private LocalDateTime updatedOn;

    @NotNull
    @Column(columnDefinition = "varchar(255) default current_user")
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        updatedOn = LocalDateTime.now();
        if (updatedBy==null) updatedBy = "app_user";
        createdOn = LocalDateTime.now();
        if (createdBy==null) createdBy = updatedBy;
        isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
        if (updatedBy==null) updatedBy = "app_user";
    }

}
