package com.todocodeacademy.products_service.controller;

import com.todocodeacademy.products_service.model.Product;
import com.todocodeacademy.products_service.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService prodSer;

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/get")
    public List<Product> getProducts(){
        return prodSer.getProducts();
    }

    @GetMapping("/get/{id}")
    public Product findProduct(@PathVariable Long id){
        //Load Balancer test
        //System.out.println("I am using the port "+serverPort);

        return prodSer.findProduct(id);
    }

    @PostMapping("/save")
    public String saveProduct(@RequestBody Product prod){
        prod.setStatus(true);
        return prodSer.saveProduct(prod);
    }

    @PutMapping("/edit/{id}")
    public String editProduct(@RequestBody Product prod, @PathVariable Long id){
        return prodSer.editProduct(prod, id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        return prodSer.deleteProduct(id);
    }

}
