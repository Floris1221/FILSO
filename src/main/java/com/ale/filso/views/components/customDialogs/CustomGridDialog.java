package com.ale.filso.views.components.customDialogs;

import com.ale.filso.models.Dictionary.Dictionary;
import com.ale.filso.views.components.Enums.ButtonType;
import com.ale.filso.views.components.customField.CustomButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class CustomGridDialog<E> extends CustomDialog {


    public Grid<E> grid;
    protected E selectedEntity;

    protected CustomGridDialog(String title, Grid<E> grid,
                               E selectedEntity){
        super(title);
        this.grid = grid;
        this.selectedEntity = selectedEntity;

        setDraggable(true);
        setResizable(true);

    }


    @Override
    protected void createView(){
        add(createDialogLayout());
        createGrid();
        createActionDialog();
    }


    private Component createDialogLayout(){
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.addItemDoubleClickListener(event -> createActionButton());

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                selectedEntity = event.getValue();
            } else {    // select last selected entity
                grid.select(selectedEntity);
            }
        });

        //Get elements to grid
        //addAttachListener(attachEvent -> {
        updateGridDataListWithSearchField();
        //});


        VerticalLayout dialogLayout = new VerticalLayout(grid);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("min-width", "600px")
                .set("min-height", "400px")
                .set("max-width", "100%").set("height", "100%");

        return dialogLayout;
    }

    private void createActionDialog() {
        CustomButton cancelButton = new CustomButton(ButtonType.CANCEL, true);
        cancelButton.addClickListener(e -> close());
        getFooter().add(cancelButton);

        CustomButton addButton = new CustomButton(ButtonType.ADD, true);
        addButton.addClickListener(buttonClickEvent -> {
            createActionButton();
        });
        this.getFooter().add(addButton);
    }

    public abstract void createGrid();
    public abstract void createActionButton();
    protected abstract void updateGridDataListWithSearchField();

}
