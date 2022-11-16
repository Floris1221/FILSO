package com.ale.filso.seciurity;

import com.ale.filso.models.User.Role;
import com.ale.filso.models.User.User;
import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.Set;

@Component
@SessionScope
public class UserAuthorization {

    private String appUrl;
    private AuthenticatedUser authenticatedUser;
    private User userAuth;

    private Set<Role> userRoles;


    @Autowired
    public UserAuthorization(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        this.userRoles = getUserRoles();

        this.userAuth = authenticatedUser.get().get();
        System.out.println("                ####UserAuthorization create");

        refreshUserAuthComponents();

        UI.getCurrent().access(() -> {          // get full url address using JS
            UI.getCurrent().getPage().executeJs("return window.location.href")
                    .then(String.class, location -> {
                        int slashPosition = location.indexOf('/',10);
                        appUrl=location.substring(0,slashPosition+1);
                    });});
    }


    private void refreshUserAuthComponents() {
        System.out.println("                ####UserAuthorization refresh");
        this.userRoles = getUserRoles();
    }

    public boolean hasRole(Role role){
        return userAuth.getRoles().contains(role);
    }

    public boolean hasRoles(Set<Role> roles){
        return userAuth.getRoles().stream().anyMatch(roles::contains);
    }

    public Set<Role> getUserRoles(){
        return authenticatedUser.getUserRoles();
    }

    public User getUserAuth() {
        return userAuth;
    }

    public String getAppUrl(){return appUrl;}

}
