package com.ale.filso.views.components.customDialogs;

import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.views.components.Enums.ButtonType;
import com.ale.filso.views.components.customField.CustomButton;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

public abstract class CustomFormDialog<E> extends CustomDialog {

    protected E entity;
    protected Binder<E> binder;
    protected GridListDataView<E> listDataView;
    protected CustomButton saveButton = new CustomButton(ButtonType.SAVE, true);

    protected CustomFormDialog(String title, E entity, Binder<E> binder, GridListDataView<E> listDataView){
        super(title);

        this.entity = entity;
        this.binder = binder;
        this.listDataView = listDataView;
    }

    @Override
    protected void createView() {
        VerticalLayout dialogLayout = createFormView();
        setLayout(dialogLayout);
        createActionDialog();
    }

    private void createActionDialog() {
        //cancelButton
        CustomButton cancelButton = new CustomButton(ButtonType.CANCEL, true);
        cancelButton.addClickListener(buttonClickEvent -> {
            clearForm();
            close();
        });
        getFooter().add(cancelButton);

        //saveButton
        saveButton.addClickListener(e -> saveAction());
        getFooter().add(saveButton);
    }

    private void setLayout(VerticalLayout dialogLayout) {
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "24rem").set("max-width", "100%");
        add(dialogLayout);
    }

    protected void clearForm() {
        entity = setNewEntity();
        binder.readBean(entity);
    }

    public abstract VerticalLayout createFormView();

    public abstract void saveAction();

    public abstract E setNewEntity();
}
