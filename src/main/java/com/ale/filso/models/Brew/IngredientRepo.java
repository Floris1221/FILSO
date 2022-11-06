package com.ale.filso.models.Brew;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepo extends JpaRepository<Ingredient, Integer> {

    @Query(value = "select * from ingredient where is_active = true and brew_id = ?1", nativeQuery = true)
    List<Ingredient> findActiveByBrew(Integer brewId);

    @Query(value = "select * from ingredient i left join product p on i.product_id = p.id" +
            " where (lower(p.name) like lower(concat('%', :text, '%'))" +
            " and i.brew_id = :brewId", nativeQuery = true)
    List<Ingredient> search(String text, Integer brewId);

    @Modifying
    @Query(value = "update ingredient set is_active = false, updated_on = CURRENT_DATE, updated_by = ?2 " +
            "where id = ?1", nativeQuery = true)
    void deleteActiveById(Integer id, String userName);
}
