package com.ale.filso.models.Brew;

import com.ale.filso.models.Warehouse.Product;
import com.ale.filso.models.Warehouse.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredientService {

    private IngredientRepo ingredientRepo;
    private ProductRepo productRepo;

    IngredientService(IngredientRepo ingredientRepo, ProductRepo productRepo){
        this.ingredientRepo = ingredientRepo;
        this.productRepo = productRepo;
    }

    public List<Ingredient> findAllActive(String text){
        if(text == null || text.isEmpty())
            return ingredientRepo.findAllActive();
        else
            return ingredientRepo.search(text);
    }

    @Transactional
    public Ingredient update(Ingredient entity){
        Product productToUpdate = entity.getProduct();
        productToUpdate.setQuantity(productToUpdate.getQuantity().subtract(entity.getQuantity()));
        Product product = productRepo.save(productToUpdate);
        return ingredientRepo.save(entity);
    }

    @Transactional
    public void delete(Ingredient entity, String userName){
        Product productToUpdate = entity.getProduct();
        productToUpdate.setQuantity(productToUpdate.getQuantity().add(entity.getQuantity()));
        Product product = productRepo.save(productToUpdate);
        ingredientRepo.deleteActiveById(entity.getId(), userName);
    }
}
