package com.ale.filso.models.Warehouse;

import com.ale.filso.models.AbstractEntity;
import com.ale.filso.models.Dictionary.Dictionary;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
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
     * Data ważności
     */
    @NotNull
    private LocalDateTime expirationDate;

    /**
     * Typ produktu
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_type_id", nullable = false)
    private Dictionary productType;
}
