package com.ale.filso.seciurity;


import com.ale.filso.models.User.Role;
import com.ale.filso.models.User.User;
import com.ale.filso.models.User.UserRepo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    @Autowired
    private UserRepo userRepo;

    private Optional<Authentication> getAuthentication() {

        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
    }

    public Optional<User> get() {
        return getAuthentication().map(authentication -> userRepo.findByLogin(authentication.getName()));
    }


    /**
     * szybki dostęp do zalogowanego username - wykorzystanie do entity.setUpdatedBy()
     *
     * @return
     */
    public String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication().getName();
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(SecurityConfiguration.LOGOUT_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }

    public Set<Role> getUserRoles(){
        return this.get().get().getRoles();
    }

}