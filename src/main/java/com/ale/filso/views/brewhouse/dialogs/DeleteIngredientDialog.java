package com.ale.filso.views.brewhouse.dialogs;

import com.ale.filso.models.Brew.Ingredient;
import com.ale.filso.models.Brew.IngredientService;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.components.customDialogs.CustomFormDialog;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.dao.OptimisticLockingFailureException;

public class DeleteIngredientDialog extends CustomFormDialog<Ingredient> {
    IngredientService ingredientService;
    Span span;
    GridListDataView<Product> productDataView;

    public DeleteIngredientDialog(String title, GridListDataView<Ingredient> listDataView,
                                     IngredientService ingredientService, GridListDataView<Product> productDataView) {
        super(title, new Ingredient(), new Binder<>(Ingredient.class), listDataView);
        this.ingredientService = ingredientService;
        this.productDataView = productDataView;

        createView();
    }

    @Override
    public VerticalLayout createFormView() {
        span = new Span();
        return new VerticalLayout(span);
    }

    @Override
    public void saveAction() {
        try {
            if(entity.getId() != null){
                ingredientService.delete(entity);
            }
            listDataView.removeItem(entity);
            if(!productDataView.contains(entity.getProduct()))
                productDataView.addItem(entity.getProduct());
            //productDataView.refreshAll();

            clearForm();
            close();

            Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
            Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    @Override
    public Ingredient setNewEntity() {
        Ingredient ingredient = new Ingredient();
        ingredient.setProduct(entity.getProduct());
        return ingredient;
    }

    public void setEntity(Ingredient entity){
        this.entity = entity;
        binder.readBean(entity);
        span.setText(String.format(getTranslation("ingredientView.dialog.delete.text"), entity.getProduct().getName()) + " ?");
    }
}
