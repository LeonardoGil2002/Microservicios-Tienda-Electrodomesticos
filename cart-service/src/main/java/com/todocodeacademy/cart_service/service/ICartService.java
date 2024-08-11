package com.todocodeacademy.cart_service.service;

import com.todocodeacademy.cart_service.dto.CartDTO;
import com.todocodeacademy.cart_service.model.Cart;

import java.util.List;

public interface ICartService {
    public List<CartDTO> getCarts();
    public CartDTO findCart(Long id);
    public String deleteCart(Long id);
    public String saveCart(Cart cart);
    public String editCart(Cart cart, Long id);
    public String addProductCart(Long cart_id, Long product_id);
    public String deleteProductCart(Long cart_id, Long product_id);
}
