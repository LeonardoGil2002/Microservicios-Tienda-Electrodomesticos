package com.todocodeacademy.products_service.repository;

import com.todocodeacademy.products_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.status = true")
    List<Product> getEnabledProducts();

}
