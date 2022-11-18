package com.ale.filso.models.Warehouse.DbView;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
@Table(name = "product_view")
public class ProductView {
    /**
     * Id
     */
    @Id
    private Integer id;

    /**
     * Numer zamówienia
     */
    private String orderNumber;

    /**
     * Nazwa
     */
    private String name;

    /**
     * Typ produktu
     */
    private String productType;

    /**
     * Ilość
     */
    private BigDecimal quantity;

    /**
     * Data ważności
     */
    private LocalDate expirationDate;

    /**
     * Jednostka miary
     */
    private String unitOfMeasure;

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
