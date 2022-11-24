package com.ale.filso.views.office;

import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.Dictionary.DictionaryService;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.about.AboutView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.LinkedHashMap;
import java.util.Map;

@Route(value = "office", layout = MainLayout.class)
@PageTitle("Biuro")
@RolesAllowed("admin")
@Getter
public class OfficeView extends VerticalLayout{

    private DictionaryService dictionaryService;

    protected Map<Tab, Component> contents = new LinkedHashMap<>();
    protected UserAuthorization userAuthorization;

    @Autowired
    protected OfficeView(UserAuthorization userAuthorization, DictionaryService dictionaryService) {
        this.userAuthorization = userAuthorization;
        this.dictionaryService = dictionaryService;
        buildContentAndTabs();
    }


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

        HorizontalLayout hl = new HorizontalLayout(tabs);
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

    }

    private void createDynamicTabOnFirstClick(Tab tab) {
        if(tab.getId().orElse("").equals(getTranslation("item.office.dictionary")))
            contents.replace(tab, new DictionarySearchView(this));
    }

    private void createContents() {
        contents.put(new Tab(getTranslation("item.office.dictionary")),
                new DictionarySearchView(this));
    }
}
