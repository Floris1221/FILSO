package com.ale.filso.models.Dictionary;

import com.ale.filso.models.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Dictionary extends AbstractEntity {

    @NotNull
    @Size(max = 255, message = "max. 255 znaków")
    private String name;

    @NotNull
    @Size(max = 100, message = "max. 100 znaków")
    private String shortName;

    @Size(max = 400, message = "max. 400 znaków")
    private String description;

    @NotNull
    private Integer dictionaryGroup;
}
