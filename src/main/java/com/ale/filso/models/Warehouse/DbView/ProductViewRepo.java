package com.ale.filso.models.Warehouse.DbView;

import com.ale.filso.models.Warehouse.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductViewRepo extends JpaRepository<ProductView, Integer> {

    @Query(value = "select * from product_view", nativeQuery = true)
    List<ProductView> findAllActive();

    @Query(value = "select * from product_view where id = ?1", nativeQuery = true)
    ProductView findActiveById(Integer id);

    @Query("select e from ProductView e " +
            "where(lower(e.orderNumber) like lower(concat('%', :text, '%')) " +
            "or lower(e.name) like lower(concat('%', :text, '%'))) ")
    List<ProductView> search(String text);
}
