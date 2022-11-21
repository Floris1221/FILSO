package com.ale.filso.views.brewhouse.filter;

import com.ale.filso.models.Brew.Ingredient;
import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.views.components.GridFilter;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

public class IngredientFilter extends GridFilter {

    private GridListDataView<Ingredient> dataView;

    private String name;
    private String productType;

    public IngredientFilter() {
    }

    public void setDataView(GridListDataView<Ingredient> dataView) {
        this.dataView = dataView;
        this.dataView.addFilter(this::test);
    }


    public void setName(String name) {
        this.name = name;
        this.dataView.refreshAll();
    }

    public void setProductType(String productType) {
        this.productType = productType;
        this.dataView.refreshAll();
    }

    public boolean test(Ingredient entity) {
        boolean matchesFirstCondition = matches(entity.getProductView().getName(), name);
        boolean matchesSecondCondition = matches(entity.getProductView().getProductType(), productType);

        return matchesFirstCondition; //&& matchesSecondCondition;
    }

}
