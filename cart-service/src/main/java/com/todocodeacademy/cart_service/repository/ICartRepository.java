package com.todocodeacademy.cart_service.repository;

import com.todocodeacademy.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICartRepository extends JpaRepository<Cart,Long> {

    @Query("SELECT c FROM Cart c WHERE c.status = true")
    List<Cart> getEnabledCarts();

}
