package com.ale.filso.views.components;

import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.views.components.brewhouse.BrewHouseSearchView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;

import java.util.Map;

public class CustomView extends Div implements BeforeEnterObserver, BeforeLeaveObserver {

    protected AuthenticatedUser authenticatedUser;

    protected CustomView(AuthenticatedUser authenticatedUser){
        this.authenticatedUser = authenticatedUser;
    }


    protected void navigateTo(Map<String, String> params){
        UI.getCurrent().navigate(BrewHouseSearchView.class, new RouteParameters(params));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {

    }
}
