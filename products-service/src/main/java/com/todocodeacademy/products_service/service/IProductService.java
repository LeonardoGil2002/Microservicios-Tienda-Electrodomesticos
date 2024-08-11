package com.todocodeacademy.products_service.service;

import com.todocodeacademy.products_service.model.Product;

import java.util.List;

public interface IProductService {

    public List<Product> getProducts();
    public Product findProduct(Long id);
    public String deleteProduct(Long id);
    public String saveProduct(Product prod);
    public String editProduct(Product prod, Long id);

}
