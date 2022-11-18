package com.ale.filso.views.components;

import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CustomDetailView<E> extends VerticalLayout implements BeforeEnterObserver,
        HasTabs, HasDynamicTitle{

    protected Integer id;
    public E entity;
    protected Map<Tab, Component> contents = new LinkedHashMap<>();
    boolean addNewObject = false;
    protected UserAuthorization userAuthorization;

    protected CustomDetailView(UserAuthorization userAuthorization, E entity) {
        this.entity = entity;
        this.userAuthorization = userAuthorization;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        try {
            id = Integer.valueOf(event.getRouteParameters().get("id").get());
            if(id == 0)
                addNewObject = true;
            entity = getEditedObjectById(id);
            buildContentAndTabs();
        }catch (Exception e){
            Notification.show(getTranslation("app.message.routeError")).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    protected abstract E getEditedObjectById(Integer id);
    protected abstract String getBackRoute();
    protected abstract void createContents();
    protected abstract void createDynamicTabOnFirstClick(Tab tab);
    protected abstract String getPageTitleObjectName();

    private void buildContentAndTabs() {

        // tabs component
        createContents();

        final Tabs tabs = new Tabs();

        // display area
        final Div display = new Div();

        display.setSizeFull();
        display.setClassName("flex flex-col");

        this.setSpacing(false);
        this.setPadding(false);
        setSizeFull();

        Icon backIcon = new Icon("vaadin", "arrow-left");
        backIcon.getStyle().set("font-size","0.8rem");
        backIcon.getStyle().set("align-self","center");
        backIcon.addClickListener(event -> UI.getCurrent().navigate(getBackRoute()));
        backIcon.getElement().setAttribute("title",getTranslation("app.message.back"));
        backIcon.getElement().getThemeList().add("badge primary");

        HorizontalLayout hl = new HorizontalLayout(backIcon, tabs);
        hl.setPadding(true);

        // show components on the screen
        this.add(hl, display);

        // update display area whenever tab selection changes
        tabs.addSelectedChangeListener(event -> {
            // remove old contents, if there was a previously selected tab
            if (event.getPreviousTab() != null) display.remove(contents.get(event.getPreviousTab()));
            // add new contents, if there is a currently selected tab
            if (event.getSelectedTab() != null) {
                if (contents.get(event.getSelectedTab())==null)    // create tab on first click
                    createDynamicTabOnFirstClick(event.getSelectedTab());
                display.add(this.contents.get(event.getSelectedTab()));
            }
        });

        // add tabs to the component
        tabs.add(this.contents.keySet().toArray(new Tab[0]));

        boolean firstTab=true;
        if (addNewObject) {     // new object - only first tab enabled
            for (Tab tab : this.contents.keySet()) {
                tab.setEnabled(firstTab);
                firstTab = false;
            }

        }
    }

    @Override
    public void setAnotherTabsEnabled(boolean isDataModifiedInTab, Component component) {
        this.contents.forEach((tab, comp) -> {
            if (!component.equals(comp)) tab.setEnabled(!isDataModifiedInTab); });

    }

    @Override
    public String getPageTitle() {

        String objectTitle = getTranslation("app.title."+this.getClass().getSimpleName());
        String objectTitleSuffix;

        if (getPageTitleObjectName()==null) {
            objectTitleSuffix = getTranslation("app.menu.label.addNew");
        } else {
            objectTitleSuffix = getPageTitleObjectName();
        }
        return objectTitle + " " + objectTitleSuffix;
    }

}
