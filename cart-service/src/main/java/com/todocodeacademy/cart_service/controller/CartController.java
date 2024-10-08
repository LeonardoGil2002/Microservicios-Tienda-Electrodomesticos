package com.todocodeacademy.cart_service.controller;

import com.todocodeacademy.cart_service.dto.CartDTO;
import com.todocodeacademy.cart_service.model.Cart;
import com.todocodeacademy.cart_service.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private ICartService cartServ;

    @GetMapping("/get")
    public List<CartDTO> getCarts(){
        return cartServ.getCarts();
    }

    @GetMapping("/get/{id}")
    public CartDTO findCart(@PathVariable Long id){
        return cartServ.findCart(id);
    }

    @GetMapping("/get/productsid/{cart_id}")
    public List<Long> findCartProductsId(@PathVariable Long cart_id){ return cartServ.findCartProductsId(cart_id);}

    @PostMapping("/save")
    public String saveCart(@RequestBody Cart cart){
        return cartServ.saveCart(cart);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCart(@PathVariable Long id){
        return cartServ.deleteCart(id);
    }

    @PutMapping("/edit/{id}")
    public String editCart(@RequestBody Cart cart, @PathVariable Long id){
        return cartServ.editCart(cart, id);
    }

    @PutMapping("/assign/{cart_id}/{sale_id}")
    public void assignSale(@PathVariable Long cart_id, @PathVariable Long sale_id){ cartServ.assignSale(cart_id, sale_id);}

    @PutMapping("/unassign/{cart_id}")
    public void unassignSale(@PathVariable Long cart_id) { cartServ.unassignSale(cart_id);}

    @PutMapping("/add/{cart_id}/{product_id}")
    public String addProductCart(@PathVariable Long cart_id, @PathVariable Long product_id){
        return cartServ.addProductCart(cart_id, product_id);
    }

    @PutMapping("/delete/{cart_id}/{product_id}")
    public String deleteProductCart(@PathVariable Long cart_id, @PathVariable Long product_id){
        return cartServ.deleteProductCart(cart_id, product_id);
    }

}
