package com.ale.filso.models;

import lombok.Getter;

import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Brew extends AbstractEntity{

    /**
     * Nazwa
     */
    @NotNull
    @Size(max = 300, message = "Max 300 znaków")
    private String name;

    /**
     * Numer
     */
    @NotNull
    private Integer number;

    /**
     * Zakładany BLG
     */
    @NotNull
    private BigDecimal assumedBlg; //one decimal place

//    /**
//     * Rzeczywisty BLG
//     */
//    private BigDecimal realBlg;

    /**
     * Zakładana ilość
     */
    @NotNull
    private Integer assumedAmount;

//    /**
//     * Rzeczywisty ilość
//     */
//    private BigDecimal realAmount;

    //todo Lista blg - mierzenie w trakcie fermentacji

//    /**
//     * Zawartość alkoholu
//     */
//    private BigDecimal alcohol;

    //todo Składniki

    //todo Zbiornik

}
