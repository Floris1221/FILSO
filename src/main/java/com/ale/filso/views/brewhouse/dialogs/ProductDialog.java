package com.ale.filso.views.brewhouse.dialogs;

import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.models.Warehouse.ProductService;
import com.ale.filso.views.components.customDialogs.CustomGridDialog;
import com.ale.filso.views.components.customField.BufferedEntityFiltering;
import com.ale.filso.views.warehouse.filter.WareHouseFilter;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;

import java.time.format.DateTimeFormatter;

import static com.ale.filso.APPCONSTANT.PRODUCT_TYPE;

public class ProductDialog extends CustomGridDialog<Product> {

    DictionaryCache dictionaryCache;
    ProductService productService;
    WareHouseFilter productFilter = new WareHouseFilter();

    AddIngredientDialog addEditIngredientDialog;
    public ProductDialog(String title, DictionaryCache dictionaryCache, ProductService productService,
                         AddIngredientDialog addEditIngredientDialog){
        super(title, new Grid<>(Product.class, false), new Product());
        this.dictionaryCache = dictionaryCache;
        this.productService = productService;
        this.addEditIngredientDialog = addEditIngredientDialog;

        createView();
    }

    @Override
    public void createGrid() {

        grid.addColumn(Product::getName).setKey("col1")
                .setHeader(getTranslation("models.product.name")).setFlexGrow(1);

        grid.addColumn(item -> getDictName(item.getProductType())).setKey("col2")
                .setHeader(getTranslation("models.product.productType")).setFlexGrow(1);

        grid.addColumn(item -> item.getQuantity() + " " + item.getUnitOfMeasure().getShortName()).setKey("col3")
                .setHeader(getTranslation("models.product.quantity")).setFlexGrow(2);

        grid.addColumn(item -> item.getExpirationDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))).setKey("col4")
                .setClassNameGenerator(Product::getExpirationColor)
                .setHeader(getTranslation("models.product.expirationDate")).setFlexGrow(1);

        // filtering
        BufferedEntityFiltering filtering = new BufferedEntityFiltering();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(grid.getColumnByKey("col1")).setComponent(
                filtering.createTextFilterHeader(productFilter::setName));
        headerRow.getCell(grid.getColumnByKey("col2")).setComponent(
                filtering.createComboFilterHeaderDictionary(productFilter::setProductType,
                        dictionaryCache.findByGroup(PRODUCT_TYPE)));
    }

    @Override
    public void createActionButton() {
        addEditIngredientDialog.setProduct(selectedEntity);
        addEditIngredientDialog.open();
    }

    @Override
    protected void updateGridDataListWithSearchField() {
        productFilter.setDataView(grid.setItems(productService.findAllActive(null)));
    }
}
