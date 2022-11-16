package com.ale.filso.views.warehouse.dialogs;

import com.ale.filso.models.Dictionary.Dictionary;
import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.models.Warehouse.ProductService;
import com.ale.filso.views.components.customDialogs.CustomFormDialog;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.dao.OptimisticLockingFailureException;

import static com.ale.filso.APPCONSTANT.PRODUCT_TYPE;
import static com.ale.filso.APPCONSTANT.UNIT_OF_MEASURE;

public class AddProductDialog extends CustomFormDialog<Product> {

    DictionaryCache dictionaryCache;
    ProductService productService;

    public AddProductDialog(String title, DictionaryCache dictionaryCache, ProductService productService, GridListDataView<Product> listDataView) {
        super(title, new Product(), new Binder<>(Product.class), listDataView);
        this.dictionaryCache = dictionaryCache;
        this.productService = productService;

        createView();
    }

    @Override
    public VerticalLayout createFormView() {
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

        return new VerticalLayout(orderNumberField, nameField, productTypeField,
                expirationDateField, quantityLayout);
    }

    @Override
    public void saveAction() {
        try {
            binder.writeBean(entity);
            entity = productService.update(entity);

            Notification.show(getTranslation("app.message.saveOk")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            listDataView.addItem(entity);



            clearForm();
            close();

        } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
            Notification.show(getTranslation("app.message.saveErrorOptimisticLock")).addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (ValidationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Product setNewEntity() {
        return new Product();
    }


}
