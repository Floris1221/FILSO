package com.ale.filso.models.CIP;

import com.ale.filso.models.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class CIP extends AbstractEntity {
    private String name;
}
