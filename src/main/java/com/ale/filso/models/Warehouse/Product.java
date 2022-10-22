package com.ale.filso.models.Warehouse;

import com.ale.filso.models.AbstractEntity;
import com.ale.filso.models.Dictionary.Dictionary;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(indexes = {
        @Index(name = "idx_product_product_type_id", columnList = "product_type_id, unit_of_measure_id")
})
@Getter
@Setter
public class Product extends AbstractEntity {

    /**
     * Numer zamówienia
     */
    @Size(max = 200, message = "Max 200 znków")
    private String orderNumber;

    /**
     * Nazwa
     */
    @Size(max = 300, message = "Max 300 znków")
    private String name;

    /**
     * Ilość
     */
    @NotNull
    private BigDecimal quantity;

    /**
     * Jednostka miary
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "unit_of_measure_id", nullable = false)
    private Dictionary unitOfMeasure;

    /**
     * Data ważności
     */
    @NotNull
    private LocalDate expirationDate;

    /**
     * Typ produktu
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_type_id", nullable = false)
    private Dictionary productType;


    /**
     * Return color of expiration
     * @return
     */
    public String getExpirationColor() {
        if (this.expirationDate==null) return null;
        long days = ChronoUnit.DAYS.between(LocalDate.now(),this.expirationDate);
        if(days <= 0) return "background-error";
        if (days <= 14) return "background-beforeError";
        if(days <= 30) return "background-warn";
        return null;
    }
}
