package com.ale.filso.views.components.brewhouse;

import com.ale.filso.models.Brew;
import com.ale.filso.models.User.Role;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.components.CustomButton;
import com.ale.filso.views.components.CustomGridView;
import com.ale.filso.views.components.Enums.ButtonType;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;



@Route(value = "brewhousesearch", layout = MainLayout.class)
@PageTitle("Warzelnia")
public class BrewHouseSearchView extends CustomGridView<Brew> {


    BrewHouseSearchView(AuthenticatedUser authenticatedUser){
        super(authenticatedUser, new Grid<>(Brew.class, false), new Brew());
        createView();
    }
    @Override
    protected void createButtonsPanel() {
        addButtonToTablePanel(ButtonType.ADD, authenticatedUser.hasRole(Role.ADMIN))
                .addClickListener(event -> detailsAction(event.isCtrlKey(),0));

        addButtonToTablePanel(ButtonType.DETAILS,true)
                .addClickListener(event -> detailsAction(event.isCtrlKey(),selectedEntity.getId()));

        grid.addItemDoubleClickListener(event -> detailsAction(event.isCtrlKey(),selectedEntity.getId()));  // grid double click action

        grid.asSingleSelect().addValueChangeListener(event -> {     // grid select action
            if (event.getValue() != null) {
                selectedEntity = event.getValue();
            } else {    // select last selected entity
                grid.select(selectedEntity);
            }
        });
    }

    //Być może wywalić do CustomGridView
    private void detailsAction(boolean isCtrlKey, Integer id) {
//        if (isCtrlKey) callUrlInNewBrowserTab(ROUTE_TEMPLATE_PROJECT,id);
//        else UI.getCurrent().navigate(String.format(ROUTE_TEMPLATE_PROJECT, id));

    }

    @Override
    protected void createGrid() {
        grid.addColumn(Brew::getNumber).setKey("col1")
                .setHeader(getTranslation("models.brew.number")).setFlexGrow(1);

        grid.addColumn(Brew::getName).setKey("col2")
                .setHeader(getTranslation("models.brew.name")).setFlexGrow(1);

        setResizeableSortableGrid(null,null);

        createSearchField();

    }
}
