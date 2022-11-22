package com.ale.filso.views.warehouse.dialogs;

import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.Warehouse.DbView.ProductView;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.models.Warehouse.ProductService;
import com.ale.filso.views.components.customDialogs.CustomFormDialog;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.dao.OptimisticLockingFailureException;

public class DeleteProductDialog extends CustomFormDialog<Product> {

    ProductService productService;
    GridListDataView<ProductView> listDataView;
    ProductView productView;

    public DeleteProductDialog(String title, ProductService productService, GridListDataView<ProductView> listDataView){
        super(title, new Product(), new Binder<>(Product.class), null);

        this.productService = productService;
        this.listDataView = listDataView;
        createView();
    }
    @Override
    public VerticalLayout createFormView() {

        Span span = new Span(getTranslation("warehouseView.dialog.delete.text"));

        TextArea deleteReasonField = new TextArea();
        binder.forField(deleteReasonField)
                .asRequired(getTranslation("app.validation.notEmpty"))
                .bind(Product::getDeleteReason, Product::setDeleteReason);

        binder.readBean(entity);

        return new VerticalLayout(span, deleteReasonField);
    }

    @Override
    public void saveAction() {
        try {
            binder.writeBean(entity);
            productService.delete(entity);

            Notification.show(getTranslation("app.message.deleteOK")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            listDataView.removeItem(productView);
            listDataView.refreshAll();


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

    public void setEntity(ProductView productView){
        this.productView = productView;
        this.entity = productService.findProductById(productView.getId());
    }

}
