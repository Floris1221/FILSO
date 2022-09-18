package com.ale.filso.models.Brew;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrewRepo extends JpaRepository<Brew, Integer> {

    @Query("SELECT e FROM Brew e WHERE e.isActive = true")
    List<Brew> getAll();

    @Query("SELECT e FROM Brew e WHERE e.id = ?1 AND e.isActive = true")
    Brew getBrewById(Integer id);
}
