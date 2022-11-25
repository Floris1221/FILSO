package com.ale.filso.views.brewhouse.dialogs;

import com.ale.filso.models.Brew.Ingredient;
import com.ale.filso.models.Brew.IngredientService;
import com.ale.filso.models.Warehouse.DbView.ProductView;
import com.ale.filso.models.Warehouse.ProductService;
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
    GridListDataView<ProductView> productGridListDataView;
    ProductService productService;
    Integer brewId;
    ProductView productView;

    public AddIngredientDialog(String title, GridListDataView<Ingredient> listDataView,
                               IngredientService ingredientService, Integer brewId, ProductService productService){
        super(title, new Ingredient(), new Binder<>(Ingredient.class), listDataView);
        this.ingredientService = ingredientService;
        this.brewId = brewId;
        this.productService = productService;


        createView();
    }
    @Override
    public VerticalLayout createFormView() {

        CustomBigDecimalField remainedQuantity = new CustomBigDecimalField(getTranslation("app.ingredientView.remainedQuantity"), null, false);
        binder.forField(remainedQuantity)
                .bindReadOnly(item -> item.getProductView().getQuantity().add(
                        item.getQuantity() == null ? new BigDecimal(0) : item.getQuantity()));

        CustomBigDecimalField quantityField = new CustomBigDecimalField(getTranslation("models.product.quantity"),null, false);
        quantityField.getStyle().set("margin-right", "30px");
        binder.forField(quantityField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .withValidator(field -> field.compareTo(remainedQuantity.getValue()) <= 0, getTranslation("ingredientView.dialog.add.quantityVerify"))
                .bind(Ingredient::getQuantity, Ingredient::setQuantity);

        TextField unitOfMeasure = new TextField(getTranslation("models.product.unitOfMeasureShortCut"));
        unitOfMeasure.getStyle().set("width", "4em");
        binder.forField(unitOfMeasure)
                .bindReadOnly(item -> item.getProductView().getUnitOfMeasure());

        HorizontalLayout h1 = new HorizontalLayout(quantityField, remainedQuantity, unitOfMeasure);

        return new VerticalLayout(h1);
    }

    @Override
    public void saveAction() {
        try {
            boolean isNewEntity = entity.getId() == null;
            //jeżeli edytujesz to ustawiasz ilość do wykorzystaia na ilość produktu + ilość już użyta w składniku
            if(!isNewEntity)
                entity.getProductView().setQuantity(entity.getProductView().getQuantity().add(entity.getQuantity()));

            entity.setProductId(entity.getProductView().getId());
            entity.setBrewId(brewId);
            binder.writeBean(entity);
            entity = ingredientService.update(entity); //zapis składnika

            Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            if(isNewEntity){ //jeżeli nowo dodany
                listDataView.addItem(entity); //dodajemy do listy składników (okno głowne)
                if(entity.getProductView().getQuantity().compareTo(new BigDecimal(0)) == 0) //sprawdzamy czy nie używamy całego produktu
                    productGridListDataView.removeItem(entity.getProductView()); //jeśli tak to usuwamy go z listy
                productGridListDataView.refreshAll();
            }
            listDataView.refreshAll();

            clearForm();
            close();

        } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
            Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (ValidationException ex) {
            throw new RuntimeException(ex);
        }
    }

    //Po updajce nowego składnika - tylko na potrzeby zamknięcia okna
    @Override
    public Ingredient setNewEntity() {
        Ingredient ingredient = new Ingredient();
        ingredient.setProductView(entity.getProductView());
        return ingredient;
    }

    //z okna dialogowego produktu - wybrany produkt, którego ccemy użyć

    /**
     * set product you want to use
     * @param productView
     */
    public void setProduct(ProductView productView){
        this.productView = productView;
        entity.setProductView(productView);
        binder.readBean(entity);
        setHeaderTitle(productView.getName() + " ["+productView.getProductType()+"]");
    }

    //z listy składników - skladnik do edycji

    /**
     * Set entity to edit
     * @param entity
     */
    public void setEntity(Ingredient entity){
        this.entity = entity;
        entity.setProductView(productService.findPVById(entity.getProductId()));
        binder.readBean(entity);
        setHeaderTitle(entity.getProductView().getName() + " ["+entity.getProductView().getProductType()+"]");
    }

    /**
     * set product list - only use in inicjalize this dialog
     * @param productGridListDataView
     */
    public void setProductGridListDataView(GridListDataView<ProductView> productGridListDataView){
        this.productGridListDataView = productGridListDataView;
    }
}
