package com.ale.filso.views.brewhouse;

import com.ale.filso.models.Brew.Brew;
import com.ale.filso.models.Brew.BrewService;
import com.ale.filso.models.Brew.IngredientService;
import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.Warehouse.ProductService;
import com.ale.filso.seciurity.AuthenticatedUser;
import com.ale.filso.seciurity.UserAuthorization;
import com.ale.filso.views.MainLayout;
import com.ale.filso.views.components.CustomDetailView;
import com.ale.filso.views.office.DictionarySearchView;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

import static com.ale.filso.APPCONSTANT.ROUTE_BREW_SEARCH;

@Route(value = "brew/:id?", layout = MainLayout.class)
@RolesAllowed("user")
@Getter
public class BrewDetailsView extends CustomDetailView<Brew>{

    private BrewService service;
    private IngredientService ingredientService;
    private DictionaryCache dictionaryCache;
    private ProductService productService;

    @Autowired
    protected BrewDetailsView(UserAuthorization userAuthorization, BrewService service, DictionaryCache dictionaryCache,
                              IngredientService ingredientService, ProductService productService) {
        super(userAuthorization, new Brew());
        this.service = service;
        this.dictionaryCache = dictionaryCache;
        this.ingredientService = ingredientService;
        this.productService = productService;

    }

    @Override
    protected Brew getEditedObjectById(Integer id) {
        if(id == 0)
            return new Brew();
        else
            return service.getBrewById(id);
    }

    @Override
    protected String getBackRoute() {
        return ROUTE_BREW_SEARCH;
    }

    @Override
    protected void createContents() {
        contents.put(new Tab(getTranslation("item.brew.brew")),
                new BrewFormView(this, entity));
        contents.put(new Tab(getTranslation("item.brew.ingredients")),
                new IngredientSearchView(this));    // create on first tab click
    }

    @Override
    protected void createDynamicTabOnFirstClick(Tab tab) {
        if(tab.getId().orElse("").equals(getTranslation("item.brew.brew")))
            contents.replace(tab, new BrewFormView(this, entity));
        else if(tab.getId().orElse("").equals(getTranslation("item.brew.ingredients")))
            contents.replace(tab, new IngredientSearchView(this));
    }

    @Override
    protected String getPageTitleObjectName() {
        return entity.getName();
    }
}
