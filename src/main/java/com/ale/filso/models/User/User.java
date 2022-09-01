package com.ale.filso.models.User;

import com.ale.filso.models.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    /**
     * Imię
     */
    @Size(max = 300, message = "max. 300 znaków")
    @NotEmpty
    private String firstName;

    /**
     * Nazwisko
     */
    @Size(max = 500, message = "max. 500 znaków")
    @NotEmpty
    private String lastName;

    /**
     * Email
     */
    @NotEmpty
    @Email(message = "Email is not valid")
    private String email;

    /**
     * Login
     */
    @NotEmpty
    private String login;

    /**
     * Hasło
     */
    @JsonIgnore
    private String hashedPassword;

    /**
     * Role
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    /**
     * Pełna nazwa
     */
    @Transient
    private String getFullName(){
        return firstName + " " + lastName;
    }
}
