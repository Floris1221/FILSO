package com.ale.filso.models.Brew;

import com.ale.filso.models.AbstractEntity;
import com.ale.filso.models.Warehouse.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(indexes = {
        @Index(name = "idx_ingredient_brewid", columnList = "brewId"),
        @Index(name = "idx_ingredient_product_id", columnList = "product_id")
})
public class Ingredient extends AbstractEntity {

    /**
     * Warka
     */
    @NotNull
    private Integer brewId;

    /**
     * Produkt
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Ilość
     */
    private BigDecimal quantity;
}
