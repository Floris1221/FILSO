package com.ale.filso.views.about;

import com.ale.filso.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.PermitAll;

@PageTitle("Informacje")
@Route(value = "about", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
//@RolesAllowed("admin")
@PermitAll
public class AboutView extends VerticalLayout {

    public AboutView() {
        //setSpacing(false);
        H2 h2 = new H2("Thank you for choosing FILSO");
        Image img = new Image("images/FILSO.png", "placeholder plant");
        img.setWidth("500px");
        add(h2, img);


        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}
