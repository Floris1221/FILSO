package com.ale.filso.models.Brew;

import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.models.Warehouse.ProductRepo;
import com.ale.filso.seciurity.UserAuthorization;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class IngredientService {

    private IngredientRepo ingredientRepo;
    private ProductRepo productRepo;
    private UserAuthorization userAuthorization;

    IngredientService(IngredientRepo ingredientRepo, ProductRepo productRepo, UserAuthorization userAuthorization){
        this.ingredientRepo = ingredientRepo;
        this.productRepo = productRepo;
        this.userAuthorization = userAuthorization;
    }

    public List<Ingredient> findAllActive(String text, Integer brewId){
        if(text == null || text.isEmpty())
            return ingredientRepo.findActiveByBrew(brewId);
        else
            return ingredientRepo.search(text, brewId);
    }

    @Transactional
    public Ingredient update(Ingredient entity){
        Product productToUpdate = productRepo.findProductById(entity.getProductId()); //find product to update
        productToUpdate.setQuantity(entity.getProductView().getQuantity()); //set quantity for this product - used when you update ingredient and real quantity = product quantity + ingredient quantity
        productToUpdate.setQuantity(productToUpdate.getQuantity().subtract(entity.getQuantity())); // substract from product quantity, used ingredient quantity

        if(productToUpdate.getQuantity().compareTo(new BigDecimal(0)) == 0) //check if used all quantity
            productToUpdate.setActive(false);  //if yes set active false
        Product product = productRepo.save(productToUpdate); //update product

        //save ingredient
        entity.setUpdatedBy(userAuthorization.getUserLogin());
        Ingredient ingredientAfterUpdate = ingredientRepo.save(entity); //update ingredient
        entity.getProductView().setQuantity(product.getQuantity()); // set for entity -> productView new quantity of product after update
        ingredientAfterUpdate.setProductView(entity.getProductView()); //set for saved ingredient productView
        return ingredientAfterUpdate;
    }

    @Transactional
    public void delete(Ingredient entity){
        Product productToUpdate = productRepo.findProductById(entity.getProductId()); //find product to update
        productToUpdate.setQuantity(productToUpdate.getQuantity().add(entity.getQuantity())); //Change product quantity = product quantity + ingredient quantity
        if(productToUpdate.getQuantity().compareTo(new BigDecimal(0)) != 0) // if product quantity != 0
            productToUpdate.setActive(true); //if yest set active true
        Product product = productRepo.save(productToUpdate); // save product

        entity.setUpdatedBy(userAuthorization.getUserLogin());
        ingredientRepo.deleteActiveById(entity.getId()); //delete ingredient
    }
}
