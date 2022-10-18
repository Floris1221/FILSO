package com.ale.filso.models.Warehouse;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepo productRepo;

    ProductService(ProductRepo productRepo){
        this.productRepo = productRepo;
    }

    public List<Product> findAllActive(String text){
        if (text == null || text.isEmpty()) {
            return productRepo.findAllActive();
        } else {
            return productRepo.search(text);
        }

    }

    public Product findById(Integer id){
        return productRepo.findActiveById(id);
    }

    public Product update(Product entity){
        return productRepo.save(entity);
    }
}
