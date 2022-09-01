package com.ale.filso.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User, Integer> {

    @Query("select " +
            "CASE WHEN COUNT(e) > 0 THEN true " +
            "ELSE false END" +
            " from User e where e.login = ?1")
    boolean checkIfUserLoginExist(String login);

    @Query(value = "select * from users where login = ?1", nativeQuery = true)
    User findByLogin(String login);
}
