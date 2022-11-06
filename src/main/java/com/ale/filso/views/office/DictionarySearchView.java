package com.ale.filso.views.office;

import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.Dictionary.DictionaryGroup;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.components.CustomGridView;
import com.ale.filso.views.components.Enums.ButtonType;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.HashMap;

@Route(value = "dictionarySearch", layout = MainLayout.class)
@PageTitle("Słowniki")
public class DictionarySearchView extends CustomGridView<DictionaryGroup> {

    private DictionaryCache dictionaryCache;
    public DictionarySearchView(UserAuthorization userAuthorization, DictionaryCache dictionaryCache) {
        super(userAuthorization, new Grid<>(DictionaryGroup.class, false), new DictionaryGroup());

        this.dictionaryCache = dictionaryCache;
        createView();
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
        grid.addColumn(DictionaryGroup::getId).setKey("col1")
                .setHeader("Id.").setFlexGrow(1);

        grid.addColumn(DictionaryGroup::getName).setKey("col2")
                .setHeader(getTranslation("models.dictionaryGroup")).setFlexGrow(5);

        setResizeableSortableGrid(null,null);

        createSearchField();
    }

    @Override
    protected void updateGridDataListWithSearchField(String filterText) {
        grid.setItems(dictionaryCache.findAll());
    }

    private void detailsAction(Integer id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id.toString());
        navigateTo(DictionaryDetailsView.class, hashMap);
    }
}
