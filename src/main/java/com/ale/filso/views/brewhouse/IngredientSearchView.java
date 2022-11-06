package com.ale.filso.views.brewhouse;

import com.ale.filso.models.Brew.Ingredient;
import com.ale.filso.models.Brew.IngredientService;
import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.User.Role;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.models.Warehouse.ProductService;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.brewhouse.filter.IngredientFilter;
import com.ale.filso.views.components.CustomGridView;
import com.ale.filso.views.components.Enums.ButtonType;
import com.ale.filso.views.components.customField.BufferedEntityFiltering;
import com.ale.filso.views.components.customField.CustomBigDecimalField;
import com.ale.filso.views.components.customField.CustomButton;
import com.ale.filso.views.warehouse.filter.WareHouseFilter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import static com.ale.filso.APPCONSTANT.PRODUCT_TYPE;

public class IngredientSearchView extends CustomGridView<Ingredient> {

    Dialog dialog = new Dialog();
    Dialog acceptanceDialog = new Dialog();
    Binder<Ingredient> binder;
    DictionaryCache dictionaryCache;
    IngredientService ingredientService;
    ProductService productService;
    Ingredient entity;
    Product productEntity;
    IngredientFilter entityFilter = new IngredientFilter();
    WareHouseFilter productFilter = new WareHouseFilter();

    CustomButton addButton = new CustomButton(ButtonType.ADD, true);
    CustomButton cancelAcceptanceButton = new CustomButton(ButtonType.CANCEL, true);

    BrewDetailsView brewDetailsView;

    Grid<Product> productGrid;


    protected IngredientSearchView(UserAuthorization userAuthorization, DictionaryCache dictionaryCache,
                                   IngredientService ingredientService, ProductService productService,
                                   BrewDetailsView brewDetailsView) {
        super(userAuthorization, new Grid<>(Ingredient.class, false), new Ingredient());

        this.brewDetailsView = brewDetailsView;
        binder =  new Binder<>(Ingredient.class);
        entity = new Ingredient();
        entity.setBrewId(brewDetailsView.entity.getId());
        productEntity = new Product();

        this.dictionaryCache = dictionaryCache;
        this.ingredientService = ingredientService;
        this.productService = productService;

        createDialog();
        buttonsDialogActions();
        createView();
    }

    @Override
    protected void createGrid() {

        grid.addColumn(item -> item.getProduct().getName()).setKey("col1")
                .setHeader(getTranslation("models.product.name")).setFlexGrow(1);

        grid.addColumn(item -> getDictName(item.getProduct().getProductType())).setKey("col2")
                .setHeader(getTranslation("models.product.productType")).setFlexGrow(1);

        grid.addColumn(item -> item.getQuantity() + " " + item.getProduct().getUnitOfMeasure().getShortName()).setKey("col3")
                .setHeader(getTranslation("models.product.quantity")).setFlexGrow(2);

        grid.addColumn(item -> item.getProduct().getExpirationDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))).setKey("col4")
                .setClassNameGenerator(item -> item.getProduct().getExpirationColor())
                .setHeader(getTranslation("models.product.expirationDate")).setFlexGrow(1);

        //Delete button
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, ingredient) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.deleteIngredient(ingredient));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader(getTranslation("ingredientView.grid.delete"));


        // filtering
        BufferedEntityFiltering filtering = new BufferedEntityFiltering();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(grid.getColumnByKey("col1")).setComponent(
                filtering.createTextFilterHeader(entityFilter::setName));
        headerRow.getCell(grid.getColumnByKey("col2")).setComponent(
                filtering.createComboFilterHeaderDictionary(entityFilter::setProductType,
                        dictionaryCache.findByGroup(PRODUCT_TYPE)));


        setResizeableSortableGrid(null,null);

        createSearchField();
    }

    private void deleteIngredient(Ingredient entity) {
        Dialog deleteDialog = new Dialog();
        deleteDialog.setHeaderTitle(getTranslation("ingredientView.dialog.delete.header"));
        deleteDialog.add(getTranslation("ingredientView.dialog.delete.text") + " " + entity.getProduct().getName() + " ?");
        CustomButton cancelDialogButton = new CustomButton(ButtonType.CANCEL, true);
        cancelDialogButton.addClickListener(event -> {  deleteDialog.close();  });
        deleteDialog.getFooter().add(cancelDialogButton);
        CustomButton confirmDialogButton = new CustomButton(ButtonType.DELETE, userAuthorization.hasRole(Role.ADMIN));
        confirmDialogButton.addClickListener(event -> {
            try {
                if(entity.getId() != null){
                    ingredientService.delete(entity, userAuthorization.getUserAuth().getLogin());
                }
                grid.getListDataView().removeItem(entity);
                grid.getListDataView().refreshAll();
                deleteDialog.close();

                Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
                Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        deleteDialog.getFooter().add(confirmDialogButton);
        deleteDialog.open();
    }

    @Override
    protected void createButtonsPanel() {
        addButtonToTablePanel(ButtonType.ADD, userAuthorization.hasRole(Role.ADMIN))
                .addClickListener(event -> detailsAction(0));
        addButtonToTablePanel(ButtonType.DETAILS, userAuthorization.hasRole(Role.ADMIN))
                .addClickListener(event -> detailsAction(selectedEntity.getId()));

        grid.addItemDoubleClickListener(event -> detailsAction(selectedEntity.getId()));

        grid.asSingleSelect().addValueChangeListener(event -> {     // grid select action
            if (event.getValue() != null) {
                selectedEntity = event.getValue();
            } else {    // select last selected entity
                grid.select(selectedEntity);
            }
        });
    }

    private void detailsAction(Integer id) {
        if(id == 0) {
            dialog.open();
        }
        else{
            entity = selectedEntity;
            binder.readBean(entity);
            acceptanceDialog.open();
        }
    }

    @Override
    protected void updateGridDataListWithSearchField(String filterText) {
        super.updateGridDataListWithSearchField(filterText);
        // refresh filter data
        entityFilter.setDataView(grid.setItems(ingredientService.findAllActive(filterText, brewDetailsView.entity.getId())));
    }

    private void createDialog() {
        dialog.setHeaderTitle(getTranslation("app.title.product"));
        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.add(createDialogLayout());

        saveButton = new CustomButton(ButtonType.SAVE, userAuthorization.hasRole(Role.ADMIN));
        cancelButton = new CustomButton(ButtonType.CANCEL, true);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(addButton);
        //dialog.getFooter().add(saveButton);

        acceptanceDialog.add(createAcceptDialog());
        acceptanceDialog.getFooter().add(cancelAcceptanceButton);
        acceptanceDialog.getFooter().add(saveButton);
    }


    private VerticalLayout createDialogLayout() {
        productGrid = new Grid<>(Product.class, false);

        // Product grid columns
        productGrid.addColumn(Product::getName).setKey("col1")
                .setHeader(getTranslation("models.product.name")).setFlexGrow(1);

        productGrid.addColumn(item -> getDictName(item.getProductType())).setKey("col2")
                .setHeader(getTranslation("models.product.productType")).setFlexGrow(1);

        productGrid.addColumn(item -> item.getQuantity() + " " + item.getUnitOfMeasure().getShortName()).setKey("col3")
                .setHeader(getTranslation("models.product.quantity")).setFlexGrow(2);

        productGrid.addColumn(item -> item.getExpirationDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))).setKey("col4")
                .setClassNameGenerator(Product::getExpirationColor)
                .setHeader(getTranslation("models.product.expirationDate")).setFlexGrow(1);

        // filtering
        BufferedEntityFiltering filtering = new BufferedEntityFiltering();
        HeaderRow headerRow = productGrid.appendHeaderRow();
        headerRow.getCell(productGrid.getColumnByKey("col1")).setComponent(
                filtering.createTextFilterHeader(productFilter::setName));
        headerRow.getCell(productGrid.getColumnByKey("col2")).setComponent(
                filtering.createComboFilterHeaderDictionary(productFilter::setProductType,
                        dictionaryCache.findByGroup(PRODUCT_TYPE)));

        //grid selection action
        productGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                productEntity = event.getValue();
            } else {    // select last selected entity
                productGrid.select(productEntity);
            }
        });


        setResizeableSortableGrid(null,null);

        //read data
        productFilter.setDataView(productGrid.setItems(productService.findAllActive(null)));

        //Set layout
        VerticalLayout dialogLayout = new VerticalLayout(productGrid);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("min-width", "600px")
                .set("min-height", "400px")
                .set("max-width", "100%").set("height", "100%");

        return dialogLayout;
    }

    public HorizontalLayout createAcceptDialog(){

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

        HorizontalLayout dialogLayout = new HorizontalLayout(quantityField, remainedQuantity, unitOfMeasure);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("min-width", "200px")
                .set("min-height", "100px")
                .set("max-width", "100%").set("height", "100%");
        return dialogLayout;
    }

    protected void buttonsDialogActions(){
        addButton.addClickListener(e -> {
            addNewIngredient();
        });

        productGrid.addItemDoubleClickListener(event -> addNewIngredient());


        saveButton.addClickListener(e -> {
            try {
                boolean isNewEntity = entity.getId() == null;
                if(!isNewEntity)
                    entity.getProduct().setQuantity(entity.getProduct().getQuantity().add(entity.getQuantity()));
                binder.writeBean(entity);
                entity = ingredientService.update(entity);

                Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                if(isNewEntity)
                    grid.getListDataView().addItem(entity);
                grid.getListDataView().refreshAll();
                productGrid.getListDataView().refreshAll();
                if(isNewEntity)
                    grid.scrollToEnd();
                grid.select(entity);

                acceptanceDialog.close();

                entity = new Ingredient();
                entity.setBrewId(brewDetailsView.entity.getId());


            } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
                Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException ex) {
                throw new RuntimeException(ex);
            }
        });

        cancelButton.addClickListener(e -> dialog.close());
        cancelAcceptanceButton.addClickListener(e -> acceptanceDialog.close());
    }

    public void addNewIngredient(){
        acceptanceDialog.setHeaderTitle(productEntity.getName() + " ["+getDictName(productEntity.getProductType())+
                "]");
        acceptanceDialog.open();
        entity.setProduct(productEntity);
        binder.readBean(entity);
    }
}
