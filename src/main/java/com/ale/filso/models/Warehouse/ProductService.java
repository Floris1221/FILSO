package com.ale.filso.models.Warehouse;

import com.ale.filso.models.Dictionary.DictionaryCache;
import com.ale.filso.models.Warehouse.DbView.ProductView;
import com.ale.filso.models.Warehouse.DbView.ProductViewRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private ProductRepo productRepo;
    ProductViewRepo productViewRepo;


    ProductService(ProductRepo productRepo, ProductViewRepo productViewRepo){
        this.productRepo = productRepo;
        this.productViewRepo = productViewRepo;
    }

    public List<Product> findAllActive(String text){
        return productRepo.findAllActive();
    }


    public Product findProductById(Integer id){
        return productRepo.findProductById(id);
    }

    public Product update(Product entity){
        return productRepo.save(entity);
    }

    @Transactional
    public void delete(Product entity){
        productRepo.delete(entity.getId(), entity.getDeleteReason());
    }

    public ProductView transferProductToProductView(Product product, String productType, String unitOfMeasure){
        ProductView productView = new ProductView();
        productView.setId(product.getId());
        productView.setName(product.getName());
        productView.setOrderNumber(product.getOrderNumber());
        productView.setQuantity(product.getQuantity());
        productView.setExpirationDate(product.getExpirationDate());
        productView.setProductType(productType);
        productView.setUnitOfMeasure(unitOfMeasure);
        return productView;
    }


    ////////////////////////////////
    ////////////ProductView/////////
    ///////////////////////////////
    public List<ProductView> findAllActivePV(String text){
        if (text == null || text.isEmpty()) {
            return productViewRepo.findAllActive();
        } else {
            return productViewRepo.search(text);
        }
    }

    public ProductView findPVById(Integer id){
        return productViewRepo.findActiveById(id);
    }

    public List<ProductView> findAllPVByIds(List<Integer> ids){
        return productViewRepo.findAllPVByIds(ids);
    }



}
