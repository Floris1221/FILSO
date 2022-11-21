package com.ale.filso.views.warehouse.filter;

import com.ale.filso.models.Warehouse.DbView.ProductView;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.views.components.GridFilter;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

public class WareHouseFilter extends GridFilter {

    private GridListDataView<ProductView> dataView;

    private String orderNumber;
    private String name;
    private String productType;

    public WareHouseFilter() {
    }

    public void setDataView(GridListDataView<ProductView> dataView) {
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

    public boolean test(ProductView entity) {
        boolean matchesFirstCondition = matches(entity.getOrderNumber(), orderNumber);
        boolean matchesSecondCondition = matches(entity.getName(), name);
        boolean matchesThirdCondition = matches(entity.getProductType(), productType);

        return matchesFirstCondition && matchesSecondCondition && matchesThirdCondition;
    }

}
