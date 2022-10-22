package com.ale.filso.views.brewhouse.filter;

import com.ale.filso.models.Brew.Ingredient;
import com.ale.filso.models.Warehouse.Product;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

public class IngredientFilter {

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
        boolean matchesFirstCondition = matches(entity.getProduct().getName(), name);
        boolean matchesSecondCondition = matches(entity.getProduct().getProductType().getName(), productType);

        return matchesFirstCondition && matchesSecondCondition;
    }

    private boolean matches(String value, String searchTerm) {
        if (value==null) value="";
        return searchTerm == null || searchTerm.isEmpty() || value
                .toLowerCase().contains(searchTerm.toLowerCase());
    }
}
