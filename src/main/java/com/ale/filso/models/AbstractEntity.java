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

    @Column(columnDefinition = "bit NOT NULL default 1")
    private boolean isActive;

    @NotNull
    @Column(columnDefinition = "datetime2(7) default getdate()")
    private LocalDateTime createdOn;

    @NotNull
    @Column(columnDefinition = "varchar(255) default suser_sname()")
    private String createdBy;

    @NotNull
    @Column(columnDefinition = "datetime2(7) default getdate()")
    private LocalDateTime updatedOn;

    @NotNull
    @Column(columnDefinition = "varchar(255) default suser_sname()")
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
