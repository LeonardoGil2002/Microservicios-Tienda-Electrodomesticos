package com.todocodeacademy.products_service.service;


import com.todocodeacademy.products_service.model.Product;
import com.todocodeacademy.products_service.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService{

    @Autowired
    private IProductRepository prodRep;

    @Override
    public List<Product> getProducts() {
        return prodRep.getEnabledProducts();
    }

    @Override
    public Product findProduct(Long id) {
        return prodRep.findById(id).orElse(null);
    }

    @Override
    public String deleteProduct(Long id) {
        Product prod = prodRep.findById(id).orElse(null);

        if(prod!=null){
            prod.setStatus(false);
            prodRep.save(prod);
            return "Product deleted successfully";
        }

        return "The product doesn't exist";

    }

    @Override
    public String saveProduct(Product prod) {
        prodRep.save(prod);
        return "Product has been created successfully";
    }

    @Override
    public String editProduct(Product prod, Long id) {

        if(prodRep.findById(id).orElse(null)==null){
            return "The product doesn't exits";
        }
        prod.setId(id);
        prodRep.save(prod);
        return "Product edited successfully";
    }
}
