package com.ale.filso.models.Warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query(value = "select * from product where is_active = true", nativeQuery = true)
    List<Product> findAllActive();

    @Query(value = "select * from product where is_active = true and id = ?1", nativeQuery = true)
    Product findActiveById(Integer id);

    @Query("select e from Product e " +
            "where(lower(e.orderNumber) like lower(concat('%', :text, '%')) " +
            "or lower(e.name) like lower(concat('%', :text, '%'))) ")
    List<Product> search(String text);
}
