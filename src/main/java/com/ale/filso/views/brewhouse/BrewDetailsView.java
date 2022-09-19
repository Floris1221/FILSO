package com.ale.filso.views.brewhouse;

import com.ale.filso.models.Brew.Brew;
import com.ale.filso.models.Brew.BrewService;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.components.CustomDetailView;
import com.ale.filso.views.login.test;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@Route(value = "brew/:id?", layout = MainLayout.class)
@PageTitle("Szczegóły warki")
@RolesAllowed("admin")
public class BrewDetailsView extends CustomDetailView<Brew> {

    BrewService service;

    protected BrewDetailsView(AuthenticatedUser authenticatedUser, BrewService service) {
        super(authenticatedUser, new Brew());
        this.service = service;
    }

    @Override
    protected Brew getEditedObjectById(Integer id) {
        System.out.println("Id w brewdetailsView: "+id);
        if(id == 0)
            return new Brew();
        else
            return service.getBrewById(id);
    }

    @Override
    protected String getBackRoute() {
        return "brewhousesearch";
    }

    @Override
    protected void createContents() {
        contents.put(new Tab(getTranslation("item.brew.brew")),
                new BrewFormView(authenticatedUser, service, entity));
        contents.put(new Tab(getTranslation("item.brew.ingredients")), new test(authenticatedUser));    // create on first tab click
    }

    @Override
    protected void createDynamicTabOnFirstClick(Tab tab) {
        if(tab.getId().orElse("").equals(getTranslation("item.brew.brew")))
            contents.replace(tab, new BrewFormView(authenticatedUser, service, entity));
        else if(tab.getId().orElse("").equals(getTranslation("item.brew.ingredients")))
            contents.replace(tab, new test(authenticatedUser));
    }
}
