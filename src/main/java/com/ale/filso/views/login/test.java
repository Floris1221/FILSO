package com.ale.filso.views.login;

import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.brewhouse.BrewHouseSearchView;
import com.ale.filso.views.components.CustomView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import com.vaadin.flow.component.button.Button;
import java.awt.*;

@Route(value = "test", layout = MainLayout.class)
@RouteAlias(value = "/", layout = MainLayout.class)
@PageTitle("Test")
public class test extends CustomView {

    public test(AuthenticatedUser authenticatedUser){
        super(authenticatedUser);
        VerticalLayout verticalLayout = new VerticalLayout();
        Button button = new Button("Hello");
        verticalLayout.add(button);
        button.addClickListener(x -> {
            navigateTo(BrewHouseSearchView.class,null);
        });
        this.add(verticalLayout);
    }

}
