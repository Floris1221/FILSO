package com.ale.filso.models.Brew;

import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.models.Warehouse.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class IngredientService {

    private IngredientRepo ingredientRepo;
    private ProductRepo productRepo;

    IngredientService(IngredientRepo ingredientRepo, ProductRepo productRepo){
        this.ingredientRepo = ingredientRepo;
        this.productRepo = productRepo;
    }

    public List<Ingredient> findAllActive(String text, Integer brewId){
        if(text == null || text.isEmpty())
            return ingredientRepo.findActiveByBrew(brewId);
        else
            return ingredientRepo.search(text, brewId);
    }

    @Transactional
    public Ingredient update(Ingredient entity){
        Product productToUpdate = productRepo.findProductById(entity.getProductId());
        productToUpdate.setQuantity(productToUpdate.getQuantity().subtract(entity.getQuantity()));
        if(productToUpdate.getQuantity().compareTo(new BigDecimal(0)) == 0)
            productToUpdate.setActive(false);
        Product product = productRepo.save(productToUpdate);
        Ingredient ingredientAfterUpdate = ingredientRepo.save(entity);
        entity.getProductView().setQuantity(product.getQuantity());
        ingredientAfterUpdate.setProductView(entity.getProductView());
        return ingredientAfterUpdate;
    }

    @Transactional
    public void delete(Ingredient entity){
        //Change product quantity after take product to ingredient
        Product productToUpdate = productRepo.findProductById(entity.getProductId());
        productToUpdate.setQuantity(productToUpdate.getQuantity().add(entity.getQuantity()));
        if(productToUpdate.getQuantity().compareTo(new BigDecimal(0)) != 0)
            productToUpdate.setActive(true);
        Product product = productRepo.save(productToUpdate);
        ingredientRepo.deleteActiveById(entity.getId());
    }
}
