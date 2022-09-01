package com.ale.filso.views.login;

import com.ale.filso.models.User.UserRepo;
import com.ale.filso.trash.DataGenerator;
import com.ale.filso.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.security.RolesAllowed;

@Route(value = "test", layout = MainLayout.class)
@RouteAlias(value = "/", layout = MainLayout.class)
@PageTitle("Test")
public class test extends Div {


    test(){
        super(new Span("tekst"));
    }

}
