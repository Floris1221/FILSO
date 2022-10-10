package com.ale.filso.views.warehouse;

import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.components.CustomGridView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "warehousesearch", layout = MainLayout.class)
@PageTitle("Magazyn")
public class WareHouseSearchView extends CustomGridView<Product> {



    protected WareHouseSearchView(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser, new Grid<>(Product.class, false), new Product());

        createView();
    }

    @Override
    protected void createButtonsPanel() {

    }

    @Override
    protected void createGrid() {

        grid.addColumn(Product::getOrderNumber).setKey("col1")
                .setHeader(getTranslation("models.product.orderNumber")).setFlexGrow(1);

        grid.addColumn(Product::getName).setKey("col2")
                .setHeader(getTranslation("models.product.name")).setFlexGrow(1);

        grid.addColumn(item -> getDictName(item.getProductType())).setKey("col3")
                        .setHeader(getTranslation("models.product.productType")).setFlexGrow(1);

        grid.addColumn(Product::getExpirationDate).setKey("col4")
                        .setHeader(getTranslation("models.product.expirationDate")).setFlexGrow(1);

        grid.addColumn(Product::getQuantity).setKey("col5")
                        .setHeader(getTranslation("models.product.quantity")).setFlexGrow(2);

        setResizeableSortableGrid(null,null);

        createSearchField();
    }

    @Override
    protected void updateGridDataListWithSearchField(String filterText) {

    }
}
