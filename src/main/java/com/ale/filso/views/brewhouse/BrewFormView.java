package com.ale.filso.views.brewhouse;

import com.ale.filso.models.Brew.Brew;
import com.ale.filso.models.Brew.BrewService;
import com.ale.filso.models.Brew.Ingredient;
import com.ale.filso.models.User.Role;
import com.ale.filso.models.Warehouse.DbView.ProductView;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.components.CustomDecimalFormat;
import com.ale.filso.views.components.CustomFormLayoutView;
import com.ale.filso.views.components.customField.CustomBigDecimalField;
import com.ale.filso.views.components.customField.CustomIntegerField;
import com.ale.filso.views.components.customField.CustomTextArea;
import com.ale.filso.views.components.customField.CustomTextField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.ale.filso.APPCONSTANT.ROUTE_BREW_DETAILS;


public class BrewFormView extends CustomFormLayoutView<Brew> {

    private BrewDetailsView view;
    private Grid<Ingredient> ingredientGrid = new Grid<>(Ingredient.class, false);

    protected BrewFormView(BrewDetailsView view) {
        super(view.getUserAuthorization(), view.getEntity(), new Binder<>(Brew.class));
        this.view = view;
        createPanel();
        ingredientTable();
    }



    @Override
    protected void createButtonLayout() {
        cancelButton.addClickListener(e -> clearForm());

        saveButton.addClickListener( e-> {
            try {
                boolean newRecord = entity.getId() == null;
                binder.writeBean(entity);
                entity = view.getService().update(entity);
                clearForm();

                Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                if(newRecord) {
                    callUrl(ROUTE_BREW_DETAILS, entity.getId());
                }
                else setModified(false);

            } catch (ValidationException ex) {
                Notification.show(getTranslation("app.message.saveError")).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
                Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        buttonLayout.add(saveButton, cancelButton);
    }

    @Override
    protected void createFormLayout() {

        CustomTextField nameField = new CustomTextField(getTranslation("models.brew.name"), userAuthorization.hasRole(Role.ADMIN));
        binder.forField(nameField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Brew::getName, Brew::setName);

        //todo make uniq number (chceck if duplicate)
        CustomIntegerField numberField = new CustomIntegerField(getTranslation("models.brew.number"), userAuthorization.hasRole(Role.ADMIN));
        numberField.setMin(0);
        binder.forField(numberField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Brew::getNumber, Brew::setNumber);

        CustomBigDecimalField assumedBlgField = new CustomBigDecimalField(getTranslation("models.brew.assumedBlg"), "%", true,
                userAuthorization.hasRole(Role.ADMIN));
        binder.forField(assumedBlgField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Brew::getAssumedBlg, Brew::setAssumedBlg);

        CustomIntegerField assumedAmountField = new CustomIntegerField(getTranslation("models.brew.assumedAmountField"), "l",
                userAuthorization.hasRole(Role.ADMIN));
        assumedAmountField.setMin(0);
        binder.forField(assumedAmountField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Brew::getAssumedAmount, Brew::setAssumedAmount);

        CustomTextArea recipeField = new CustomTextArea(getTranslation("models.brew.recipe"), userAuthorization.hasRole(Role.ADMIN));
        formLayout.setColspan(recipeField, 2);
        binder.forField(recipeField)
                .bind(Brew::getRecipe, Brew::setRecipe);

        //add all fields to layout
        formLayout.add(nameField, numberField, assumedBlgField, assumedAmountField, recipeField);

        clearForm();
    }


    private void ingredientTable(){

        ingredientGrid.addColumn(item -> item.getProductView().getName()).setKey("col1")
                .setHeader(getTranslation("models.product.name")).setFlexGrow(1);

        ingredientGrid.addColumn(item -> item.getProductView().getProductType()).setKey("col2")
                .setHeader(getTranslation("models.product.productType")).setFlexGrow(1);

        ingredientGrid.addColumn(new ComponentRenderer<>(item -> {
                    CustomDecimalFormat format = new CustomDecimalFormat();
                    Span span = new Span(format.format(item.getQuantity()));
                    span.setText(span.getText()+" "+item.getProductView().getUnitOfMeasure());
                    return span;
                })).setKey("col3")
                .setHeader(getTranslation("models.product.quantity")).setFlexGrow(1);

        ingredientGrid.addColumn(item -> item.getProductView().getExpirationDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))).setKey("col4")
                .setClassNameGenerator(item -> item.getProductView().getExpirationColor())
                .setHeader(getTranslation("models.product.expirationDate")).setFlexGrow(1);

        //Set items
        addAttachListener(attachEvent -> {
            List<Ingredient> ingredients = view.getIngredientService().findAllActive(null, view.getEntity().getId());
            if(!ingredients.isEmpty()){
                List<ProductView> productViews = view.getProductService().findAllPVByIds(ingredients.stream().map(Ingredient::getProductId).toList());
                for (Ingredient item: ingredients){
                    item.setProductView(productViews.stream().filter(x -> Objects.equals(item.getProductId(), x.getId())).findFirst().orElse(new ProductView()));
                }
            }
            ingredientGrid.setItems(ingredients);
        });


        //add grid to view
        this.add(ingredientGrid);

    }


}
