package com.ale.filso.views.warehouse.filter;

import com.ale.filso.models.Warehouse.Product;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

public class WareHouseFilter {

    private GridListDataView<Product> dataView;

    private String orderNumber;
    private String name;
    private String productType;

    public WareHouseFilter() {
    }

    public void setDataView(GridListDataView<Product> dataView) {
        this.dataView = dataView;
        this.dataView.addFilter(this::test);
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        this.dataView.refreshAll();
    }

    public void setName(String name) {
        this.name = name;
        this.dataView.refreshAll();
    }

    public void setProductType(String productType) {
        this.productType = productType;
        this.dataView.refreshAll();
    }

    public boolean test(Product entity) {
        boolean matchesFirstCondition = matches(entity.getOrderNumber(), orderNumber);
        boolean matchesSecondCondition = matches(entity.getName(), name);
        boolean matchesThirdCondition = matches(entity.getProductType().getName(), productType);

        return matchesFirstCondition && matchesSecondCondition && matchesThirdCondition;
    }

    private boolean matches(String value, String searchTerm) {
        if (value==null) value="";
        return searchTerm == null || searchTerm.isEmpty() || value
                .toLowerCase().contains(searchTerm.toLowerCase());
    }
}
