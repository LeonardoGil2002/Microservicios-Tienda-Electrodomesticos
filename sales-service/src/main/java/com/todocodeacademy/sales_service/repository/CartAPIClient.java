package com.todocodeacademy.sales_service.repository;

import com.todocodeacademy.sales_service.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "cartapi", url = "localhost:8081")
public interface CartAPIClient {
    @GetMapping("/carts/get/{id}")
    public CartDTO findCart(@PathVariable Long id);

    @GetMapping("/carts/get/productsid/{cart_id}")
    public List<Long> findCartProductsId(@PathVariable Long cart_id);

    @PutMapping("/carts/assign/{cart_id}/{sale_id}")
    public void assignSale(@PathVariable Long cart_id, @PathVariable Long sale_id);

    @PutMapping("/carts/unassign/{cart_id}")
    public void unassignSale(@PathVariable Long cart_id);
}
