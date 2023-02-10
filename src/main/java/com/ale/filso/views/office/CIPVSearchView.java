package com.ale.filso.views.office;

import com.ale.filso.models.CIP.CIP;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.components.CustomGridView;
import com.ale.filso.views.components.Enums.ButtonType;
import com.vaadin.flow.component.grid.Grid;

import java.util.HashMap;

public class CIPVSearchView extends CustomGridView<CIP> {
    protected CIPVSearchView(UserAuthorization userAuthorization) {
        super(userAuthorization, new Grid<>(CIP.class), new CIP());
    }

    @Override
    protected void createButtonsPanel() {

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

    @Override
    protected void createGrid() {
        grid.addColumn(CIP::getName).setKey("col1")
                .setHeader("Id.").setFlexGrow(1);

        grid.addColumn(CIP::getCreatedOn).setKey("col2")
                .setHeader(getTranslation("models.cip.name")).setFlexGrow(5);

        setResizeableSortableGrid(null,null);

        createSearchField();
    }

//    @Override
//    protected void updateGridDataListWithSearchField(String filterText) {
//        grid.setItems(view.getDictionaryService().findAll());
//    }

    private void detailsAction(Integer id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id.toString());
        //navigateTo(CIPDetailsView.class, hashMap);
    }
}
