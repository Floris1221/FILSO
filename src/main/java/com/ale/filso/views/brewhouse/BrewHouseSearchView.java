package com.ale.filso.views.brewhouse;

import com.ale.filso.models.Brew.Brew;
import com.ale.filso.models.Brew.BrewService;
import com.ale.filso.models.User.Role;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.components.CustomGridView;
import com.ale.filso.views.components.Enums.ButtonType;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.HashMap;


@Route(value = "brewhousesearch", layout = MainLayout.class)
@PageTitle("Warzelnia")
public class BrewHouseSearchView extends CustomGridView<Brew> {

    BrewService service;

    BrewHouseSearchView(AuthenticatedUser authenticatedUser, BrewService service){
        super(authenticatedUser, new Grid<>(Brew.class, false), new Brew());
        this.service = service;
        createView();
    }
    @Override
    protected void createButtonsPanel() {
        addButtonToTablePanel(ButtonType.ADD, authenticatedUser.hasRole(Role.ADMIN))
                .addClickListener(event -> detailsAction(0));

        addButtonToTablePanel(ButtonType.DETAILS,true)
                .addClickListener(event -> detailsAction(selectedEntity.getId()));

        grid.addItemDoubleClickListener(event -> detailsAction(selectedEntity.getId()));  // grid double click action

        grid.asSingleSelect().addValueChangeListener(event -> {     // grid select action
            if (event.getValue() != null) {
                selectedEntity = event.getValue();
            } else {    // select last selected entity
                grid.select(selectedEntity);
            }
        });
    }

    private void detailsAction(Integer id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id.toString());
        navigateTo(BrewDetailsView.class, hashMap);
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

    @Override
    protected void updateGridDataListWithSearchField(String filterText) {
        super.updateGridDataListWithSearchField(filterText);
        // refresh filter data
        grid.setItems(service.getAll(filterText));
        //entityFilter.setDataView(grid.setItems(projectDbViewService.findAll(filterText)));
    }
}
