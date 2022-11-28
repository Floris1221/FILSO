package com.ale.filso.views.brewhouse;

import com.ale.filso.models.Brew.Brew;
import com.ale.filso.models.Brew.BrewService;
import com.ale.filso.models.Brew.Ingredient;
import com.ale.filso.models.Dictionary.Dictionary;
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
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ale.filso.APPCONSTANT.PRODUCT_TYPE;
import static com.ale.filso.APPCONSTANT.ROUTE_BREW_DETAILS;


public class BrewFormView extends CustomFormLayoutView<Brew> {

    private BrewDetailsView view;
    private TreeGrid<Ingredient> ingredientGrid = new TreeGrid<>();
    private List<Ingredient> ingredients;

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

        //Set grid specified
        ingredientGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        ingredientGrid.setAllRowsVisible(false);
        ingredientGrid.setVerticalScrollingEnabled(false);

        ingredientGrid.addHierarchyColumn(item -> item.getProductView().getProductType()).setKey("col1")
                .setHeader(getTranslation("models.product.productType")).setFlexGrow(1);

        ingredientGrid.addColumn(item -> item.getProductView().getName()).setKey("col2")
                .setHeader(getTranslation("models.product.name")).setFlexGrow(1);

        ingredientGrid.addColumn(new ComponentRenderer<>(item -> {
                    if(item.getQuantity() == null ) return new Span();
                    CustomDecimalFormat format = new CustomDecimalFormat();
                    Span span = new Span(format.format(item.getQuantity()));
                    span.setText(span.getText()+" "+item.getProductView().getUnitOfMeasure());
                    return span;
                })).setKey("col3")
                .setHeader(getTranslation("models.product.quantity")).setFlexGrow(1);

        ingredientGrid.addColumn(item -> item.getProductView().getExpirationDate() != null ?
                        item.getProductView().getExpirationDate()
                                .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")) :
                        null)
                .setKey("col4")
                .setClassNameGenerator(item -> item.getProductView().getExpirationColor())
                .setHeader(getTranslation("models.product.expirationDate")).setFlexGrow(1);

        /////////Set items
        //on every change
        addAttachListener(attachEvent -> {
            //get ingredients
            ingredients = view.getIngredientService().findAllActive(null, view.getEntity().getId());
            //set ProductView for ingredient
            if(!ingredients.isEmpty()){
                List<ProductView> productViews = view.getProductService().findAllPVByIds(ingredients.stream().map(Ingredient::getProductId).toList());
                for (Ingredient item: ingredients){
                    item.setProductView(productViews.stream().filter(x -> Objects.equals(item.getProductId(), x.getId())).findFirst().orElse(new ProductView()));
                }
            }

            TreeData<Ingredient> treeData = new TreeData<>();

            //Get List of Ingredient types and map this as new Ingredient to show in TreeGrid
            List<Ingredient> groupItems = new ArrayList<>();
            for (Dictionary productType:view.getDictionaryCache().getDict(PRODUCT_TYPE)){
                Ingredient groupItem = new Ingredient();
                ProductView productView = new ProductView();
                productView.setProductType(productType.getName());
                groupItem.setProductView(productView);
                groupItems.add(groupItem);
            }

            //Create roots
            treeData.addRootItems(groupItems);
            //add items to root
            groupItems.forEach(group -> {
                treeData.addItems(group, ingredients.stream().filter(item ->
                        Objects.equals(group.getProductView().getProductType(), item.getProductView().getProductType())));
            });

            //add element to data provider
            TreeDataProvider<Ingredient> treeDataProvider = new TreeDataProvider<>(
                    treeData);
            ingredientGrid.setDataProvider(treeDataProvider);
            //expand elements on start
            ingredientGrid.expand(groupItems);

        });

        //add grid to view
        formLayout.add(ingredientGrid);
        formLayout.setColspan(ingredientGrid, 2);

    }

}
