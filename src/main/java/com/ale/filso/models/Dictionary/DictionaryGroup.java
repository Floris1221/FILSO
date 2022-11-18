package com.ale.filso.models.Dictionary;

import com.ale.filso.models.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
public class DictionaryGroup extends AbstractEntity {

    @NotBlank
    @Size(max = 255, message = "max. 255 znaków")
    private String name;

    @Size(max = 255, message = "max. 255 znaków")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="dictionaryGroup")
    private List<Dictionary> dictionaryList;

}
