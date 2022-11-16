package com.ale.filso.views.warehouse;

import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.User.Role;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.models.Warehouse.ProductService;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.components.CustomGridView;
import com.ale.filso.views.components.Enums.ButtonType;
import com.ale.filso.views.components.customField.*;
import com.ale.filso.views.warehouse.dialogs.AddProductDialog;
import com.ale.filso.views.warehouse.dialogs.DeleteProductDialog;
import com.ale.filso.views.warehouse.filter.WareHouseFilter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;

import static com.ale.filso.APPCONSTANT.PRODUCT_TYPE;

@Route(value = "warehousesearch", layout = MainLayout.class)
@PageTitle("Magazyn")
public class WareHouseSearchView extends CustomGridView<Product>{

    AddProductDialog addDialog;
    DeleteProductDialog deleteDialog;
    DictionaryCache dictionaryCache;
    ProductService productService;
    Product entity;
    WareHouseFilter entityFilter = new WareHouseFilter();


    protected WareHouseSearchView(UserAuthorization userAuthorization, DictionaryCache dictionaryCache,
                                  ProductService productService) {
        super(userAuthorization, new Grid<>(Product.class, false), new Product());

        entity = new Product();

        this.dictionaryCache = dictionaryCache;
        this.productService = productService;

        addDialog = new AddProductDialog(getTranslation("app.title.dictionary.new"), dictionaryCache, productService, grid.getListDataView());
        deleteDialog = new DeleteProductDialog(getTranslation("warehouseView.dialog.delete.header"), dictionaryCache, productService, grid.getListDataView());


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
        deleteDialog.setEntity(entity);
        deleteDialog.open();
    }

    @Override
    protected void createButtonsPanel() {
        addButtonToTablePanel(ButtonType.ADD, userAuthorization.hasRole(Role.ADMIN))
                .addClickListener(event -> detailsAction());
    }

    private void detailsAction() {
        addDialog.open();
    }

    @Override
    protected void updateGridDataListWithSearchField(String filterText) {
        super.updateGridDataListWithSearchField(filterText);
        // refresh filter data
        entityFilter.setDataView(grid.setItems(productService.findAllActive(filterText)));
    }

}
