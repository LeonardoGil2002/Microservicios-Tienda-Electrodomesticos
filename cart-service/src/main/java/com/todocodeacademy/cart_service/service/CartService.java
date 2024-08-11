package com.todocodeacademy.cart_service.service;

import com.todocodeacademy.cart_service.dto.CartDTO;
import com.todocodeacademy.cart_service.dto.ProductDTO;
import com.todocodeacademy.cart_service.model.Cart;
import com.todocodeacademy.cart_service.repository.ICartRepository;
import com.todocodeacademy.cart_service.repository.ProductAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService implements ICartService{

    @Autowired
    private ICartRepository cartRep;

    @Autowired
    private ProductAPIClient productapi;

    @Override
    public List<CartDTO> getCarts() {

        List<Cart> cart_list = cartRep.getEnabledCarts();
        List<CartDTO> cartDTO_list = new ArrayList<>();


        for(Cart c : cart_list){

            CartDTO cartDTO = new CartDTO();
            List<ProductDTO> productDTO_list = new ArrayList<>();

            cartDTO.setId(c.getId());
            cartDTO.setStatus(c.getStatus());
            cartDTO.setTotal(c.getTotal());

            for(Long i : c.getProduct_list()){

                ProductDTO productdto = productapi.findProduct(i);
                productDTO_list.add(productdto);

            }

            cartDTO.setProduct_list(productDTO_list);

            cartDTO_list.add(cartDTO);

        }

        return cartDTO_list;

    }

    @Override
    public CartDTO findCart(Long id) {

        Cart cart = cartRep.findById(id).orElse(null);
        CartDTO cartDTO = new CartDTO();

        if(cart!=null) {

            List<ProductDTO> productDTO_list = new ArrayList<>();

            cartDTO.setId(cart.getId());
            cartDTO.setStatus(cart.getStatus());
            cartDTO.setTotal(cart.getTotal());

            for (Long i : cart.getProduct_list()) {

                ProductDTO productdto = productapi.findProduct(i);
                productDTO_list.add(productdto);

            }

            cartDTO.setProduct_list(productDTO_list);

        }

        return cartDTO;

    }

    @Override
    public String deleteCart(Long id) {

        Cart cart = cartRep.findById(id).orElse(null);

        if(cart!=null){
            cart.setStatus(false);
            cartRep.save(cart);
            return "Cart deleted successfully";
        }

        return "The cart doesn't exist";

    }

    @Override
    public String saveCart(Cart cart) {

        Double total = 0.00;

        for (Long i : cart.getProduct_list()) {

            ProductDTO productdto = productapi.findProduct(i);

            if(productapi.findProduct(i)==null){
                return "ERROR: The product " + i + " doesn't exist";
            }

            total += productdto.getPrice();

        }

        cart.setTotal(total);
        cart.setStatus(true);

        cartRep.save(cart);
        return "Cart saved successfully";
    }

    @Override
    public String editCart(Cart cart, Long id) {

        Cart c = cartRep.findById(id).orElse(null);

        if(c!=null){
            cart.setId(id);

            Double total = 0.00;

            for (Long i : cart.getProduct_list()) {

                ProductDTO productdto = productapi.findProduct(i);

                if(productapi.findProduct(i)==null){
                    return "ERROR: The product " + i + " doesn't exist";
                }

                total += productdto.getPrice();

            }

            cart.setTotal(total);
            cart.setStatus(c.getStatus());

            cartRep.save(cart);
            return "Cart edited successfully";
        }

        return "The cart doesn't exist";
    }

    @Override
    public String addProductCart(Long cart_id, Long product_id) {

        Cart cart = cartRep.findById(cart_id).orElse(null);

        if(cart==null){
            return "The cart doesn't exist";
        }

        ProductDTO productdto = productapi.findProduct(product_id);

        if(productapi.findProduct(product_id)==null){
            return "ERROR: The product " + product_id + " doesn't exist";
        }

        List<Long> product_list = cart.getProduct_list();

        product_list.add(product_id);

        cart.setProduct_list(product_list);
        cart.setTotal(cart.getTotal() + productdto.getPrice());

        cartRep.save(cart);

        return "The product was added successfully";

    }

    @Override
    public String deleteProductCart(Long cart_id, Long product_id) {

        Cart cart = cartRep.findById(cart_id).orElse(null);

        if(cart==null){
            return "The cart doesn't exist";
        }

        ProductDTO productdto = productapi.findProduct(product_id);

        if(productapi.findProduct(product_id)==null){
            return "ERROR: The product " + product_id + " doesn't exist";
        }

        List<Long> product_list = cart.getProduct_list();

        if(product_list.remove(product_id)){

            cart.setProduct_list(product_list);
            cart.setTotal(cart.getTotal() - productdto.getPrice());
            cartRep.save(cart);

            return "Product removed successfully";

        }

        return "The product doesn't exist in the cart selected";

    }
}
