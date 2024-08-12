package com.todocodeacademy.cart_service.repository;

import com.todocodeacademy.cart_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "products-service")
public interface ProductAPIClient {

    @GetMapping("/products/get/{id}")
    public ProductDTO findProduct(@PathVariable Long id);

}
