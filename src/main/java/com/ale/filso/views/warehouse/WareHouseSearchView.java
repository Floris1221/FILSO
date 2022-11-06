package com.ale.filso.views.warehouse;

import com.ale.filso.models.Brew.Brew;
import com.ale.filso.models.Brew.Ingredient;
import com.ale.filso.models.Dictionary.Dictionary;
import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.User.Role;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.models.Warehouse.ProductService;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.components.CustomGridView;
import com.ale.filso.views.components.Enums.ButtonType;
import com.ale.filso.views.components.customField.*;
import com.ale.filso.views.warehouse.filter.WareHouseFilter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.format.DateTimeFormatter;

import static com.ale.filso.APPCONSTANT.PRODUCT_TYPE;
import static com.ale.filso.APPCONSTANT.UNIT_OF_MEASURE;

@Route(value = "warehousesearch", layout = MainLayout.class)
@PageTitle("Magazyn")
public class WareHouseSearchView extends CustomGridView<Product> {

    Dialog dialog = new Dialog();
    Binder<Product> binder;
    Binder<Product> deleteBinder;
    DictionaryCache dictionaryCache;
    ProductService productService;
    Product entity;
    WareHouseFilter entityFilter = new WareHouseFilter();


    protected WareHouseSearchView(UserAuthorization userAuthorization, DictionaryCache dictionaryCache,
                                  ProductService productService) {
        super(userAuthorization, new Grid<>(Product.class, false), new Product());

        binder =  new Binder<>(Product.class);
        deleteBinder = new Binder<>(Product.class);
        entity = new Product();

        this.dictionaryCache = dictionaryCache;
        this.productService = productService;

        createDialog();
        buttonsDialogActions();
        createView();
    }

    @Override
    protected void createGrid() {

        grid.addColumn(Product::getOrderNumber).setKey("col1")
                .setHeader(getTranslation("models.product.orderNumber")).setFlexGrow(1);

        grid.addColumn(Product::getName).setKey("col2")
                .setHeader(getTranslation("models.product.name")).setFlexGrow(1);

        grid.addColumn(item -> getDictName(item.getProductType())).setKey("col3")
                        .setHeader(getTranslation("models.product.productType")).setFlexGrow(1);

        grid.addColumn(item -> item.getQuantity() + " " + item.getUnitOfMeasure().getShortName()).setKey("col4")
                .setHeader(getTranslation("models.product.quantity")).setFlexGrow(2);

        grid.addColumn(item -> item.getExpirationDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))).setKey("col5")
                        .setClassNameGenerator(Product::getExpirationColor)
                        .setHeader(getTranslation("models.product.expirationDate")).setFlexGrow(1);


        // filtering
        BufferedEntityFiltering filtering = new BufferedEntityFiltering();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(grid.getColumnByKey("col1")).setComponent(
                filtering.createTextFilterHeader(entityFilter::setOrderNumber));
        headerRow.getCell(grid.getColumnByKey("col2")).setComponent(
                filtering.createTextFilterHeader(entityFilter::setName));
        headerRow.getCell(grid.getColumnByKey("col3")).setComponent(
                filtering.createComboFilterHeaderDictionary(entityFilter::setProductType,
                        dictionaryCache.findByGroup(PRODUCT_TYPE)));

        //Delete button
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, product) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.deleteProduct(product));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader(getTranslation("ingredientView.grid.delete"));


        setResizeableSortableGrid(null,null);

        createSearchField();
    }


    private void deleteProduct(Product entity) {
        Dialog deleteDialog = new Dialog();
        deleteDialog.setHeaderTitle(getTranslation("warehouseView.dialog.delete.header"));
        deleteDialog.add(getTranslation("warehouseView.dialog.delete.text"));

        //todo ten jebany binder chyba trzeba dać delete binder i chuj
        //Reason why
        TextArea deleteReasonField = new TextArea();
        deleteBinder.forField(deleteReasonField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Product::getDeleteReason, Product::setDeleteReason);

        deleteBinder.readBean(entity);

        HorizontalLayout h1 = new HorizontalLayout();
        h1.add(deleteReasonField);

        deleteDialog.add(h1);

        CustomButton cancelDialogButton = new CustomButton(ButtonType.CANCEL, true);
        cancelDialogButton.addClickListener(event -> {  deleteDialog.close();  });
        deleteDialog.getFooter().add(cancelDialogButton);
        CustomButton confirmDialogButton = new CustomButton(ButtonType.DELETE, userAuthorization.hasRole(Role.ADMIN));
        confirmDialogButton.addClickListener(event -> {
            try {
                deleteBinder.writeBean(entity);
                productService.delete(entity, userAuthorization.getUserAuth().getLogin());
                grid.getListDataView().removeItem(entity);
                grid.getListDataView().refreshAll();
                deleteBinder.readBean(new Product());
                deleteDialog.close();

                Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
                Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });
        deleteDialog.getFooter().add(confirmDialogButton);
        deleteDialog.open();
    }

    @Override
    protected void createButtonsPanel() {
        addButtonToTablePanel(ButtonType.ADD, userAuthorization.hasRole(Role.ADMIN))
                .addClickListener(event -> detailsAction());
    }

    private void detailsAction() {
        dialog.open();
    }

    @Override
    protected void updateGridDataListWithSearchField(String filterText) {
        super.updateGridDataListWithSearchField(filterText);
        // refresh filter data
        entityFilter.setDataView(grid.setItems(productService.findAllActive(filterText)));
    }

    private void createDialog() {
        dialog.setHeaderTitle(getTranslation("app.title.dictionary.new"));

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        saveButton = new CustomButton(ButtonType.SAVE, true);
        cancelButton = new CustomButton(ButtonType.CANCEL, true);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
    }


    private VerticalLayout createDialogLayout() {
        TextField orderNumberField = new TextField(getTranslation("models.product.orderNumber"));
        binder.forField(orderNumberField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Product::getOrderNumber, Product::setOrderNumber);

        TextField nameField = new TextField(getTranslation("models.product.name"));
        binder.forField(nameField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Product::getName, Product::setName);

        ComboBox<Dictionary> productTypeField = new ComboBox<>(getTranslation("models.product.productType"));
        productTypeField.setItems(dictionaryCache.findByGroup(PRODUCT_TYPE));
        productTypeField.setItemLabelGenerator(Dictionary::getName);
        binder.forField(productTypeField).asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Product::getProductType, Product::setProductType);

        DatePicker expirationDateField = new DatePicker(getTranslation("models.product.expirationDate"));
        binder.forField(expirationDateField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Product::getExpirationDate, Product::setExpirationDate);

        BigDecimalField quantityField = new BigDecimalField(getTranslation("models.product.quantity"));
        binder.forField(quantityField)
                        .asRequired(getTranslation("app.validation.notEmpty"))
                                .bind(Product::getQuantity, Product::setQuantity);

        ComboBox<Dictionary> unitOfMeasureField = new ComboBox<>(getTranslation("models.product.unitOfMeasure"));
        unitOfMeasureField.setItems(dictionaryCache.findByGroup(UNIT_OF_MEASURE));
        unitOfMeasureField.setItemLabelGenerator(Dictionary::getShortName);
        binder.forField(unitOfMeasureField).asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Product::getUnitOfMeasure, Product::setUnitOfMeasure);


        binder.readBean(entity);

        HorizontalLayout quantityLayout = new HorizontalLayout(quantityField, unitOfMeasureField);
        VerticalLayout dialogLayout = new VerticalLayout(orderNumberField, nameField, productTypeField,
                expirationDateField, quantityLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    protected void buttonsDialogActions(){
        saveButton.addClickListener(e -> {
            try {
                binder.writeBean(entity);
                entity = productService.update(entity);

                Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                grid.getListDataView().addItem(entity);
                grid.getListDataView().refreshAll();
                grid.scrollToEnd();
                grid.select(entity);

                dialog.close();

                entity = new Product();
                binder.readBean(entity);


            } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
                Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException ex) {
                throw new RuntimeException(ex);
            }
        });

        cancelButton.addClickListener(e -> dialog.close());
    }
}
