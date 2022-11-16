package com.ale.filso.views.brewhouse.dialogs;

import com.ale.filso.models.Brew.Ingredient;
import com.ale.filso.models.Brew.IngredientService;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.views.components.customDialogs.CustomFormDialog;
import com.ale.filso.views.components.customField.CustomBigDecimalField;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;

public class AddIngredientDialog extends CustomFormDialog<Ingredient> {

    IngredientService ingredientService;
    GridListDataView<Product> productGridListDataView;
    Integer brewId;

    public AddIngredientDialog(String title, GridListDataView<Ingredient> listDataView,
                               IngredientService ingredientService, Integer brewId){
        super(title, new Ingredient(), new Binder<>(Ingredient.class), listDataView);
        this.ingredientService = ingredientService;
        this.brewId = brewId;


        createView();
    }
    @Override
    public VerticalLayout createFormView() {

        CustomBigDecimalField remainedQuantity = new CustomBigDecimalField(getTranslation("app.ingredientView.remainedQuantity"), null, false);
        binder.forField(remainedQuantity)
                .bindReadOnly(item -> item.getProduct().getQuantity().add(
                        item.getQuantity() == null ? new BigDecimal(0) : item.getQuantity()));

        //todo zaokrąglenia nie działają
        CustomBigDecimalField quantityField = new CustomBigDecimalField(getTranslation("models.product.quantity"), null, false);
        quantityField.getStyle().set("margin-right", "30px");
        binder.forField(quantityField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .withValidator(field -> field.compareTo(remainedQuantity.getValue()) <= 0, getTranslation("ingredientView.dialog.add.quantityVerify"))
                .bind(Ingredient::getQuantity, Ingredient::setQuantity);

        TextField unitOfMeasure = new TextField(getTranslation("models.product.unitOfMeasureShortCut"));
        unitOfMeasure.getStyle().set("width", "4em");
        binder.forField(unitOfMeasure)
                .bindReadOnly(item -> getDictShortName(item.getProduct().getUnitOfMeasure()));

        HorizontalLayout h1 = new HorizontalLayout(quantityField, remainedQuantity, unitOfMeasure);

        return new VerticalLayout(h1);
    }

    @Override
    public void saveAction() {
        try {
            boolean isNewEntity = entity.getId() == null;
            if(!isNewEntity)
                entity.getProduct().setQuantity(entity.getProduct().getQuantity().add(entity.getQuantity()));

            entity.setBrewId(brewId);
            binder.writeBean(entity);
            entity = ingredientService.update(entity);

            Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            if(isNewEntity)
                listDataView.addItem(entity);
            listDataView.refreshAll();
            if(entity.getProduct().getQuantity().compareTo(new BigDecimal(0)) == 0)
                productGridListDataView.removeItem(entity.getProduct());
            productGridListDataView.refreshAll();

            clearForm();
            close();

        } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
            Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (ValidationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Ingredient setNewEntity() {
        Ingredient ingredient = new Ingredient();
        ingredient.setProduct(entity.getProduct());
        return ingredient;
    }

    public void setProduct(Product product){
        entity.setProduct(product);
        binder.readBean(entity);
        setHeaderTitle(product.getName() + " ["+getDictName(product.getProductType())+
                "]");
    }

    public void setEntity(Ingredient entity){
        this.entity = entity;
        binder.readBean(entity);
        setHeaderTitle(entity.getProduct().getName() + " ["+getDictName(entity.getProduct().getProductType())+
                "]");
    }

    public void setProductGridListDataView(GridListDataView<Product> productGridListDataView){
        this.productGridListDataView = productGridListDataView;
    }
}
