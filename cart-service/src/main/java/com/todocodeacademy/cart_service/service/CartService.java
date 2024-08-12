package com.todocodeacademy.cart_service.service;

import com.todocodeacademy.cart_service.dto.CartDTO;
import com.todocodeacademy.cart_service.dto.ProductDTO;
import com.todocodeacademy.cart_service.model.Cart;
import com.todocodeacademy.cart_service.repository.ICartRepository;
import com.todocodeacademy.cart_service.repository.ProductAPIClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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

    private final CartDTO cartError = new CartDTO(0L, null, null, null, null);

    @Override
    @CircuitBreaker(name="products-service",fallbackMethod="fallbackCartList")
    @Retry(name="products-service")
    public List<CartDTO> getCarts() {

        List<Cart> cart_list = cartRep.getEnabledCarts();
        List<CartDTO> cartDTO_list = new ArrayList<>();


        for(Cart c : cart_list){

            CartDTO cartDTO = new CartDTO();
            List<ProductDTO> productDTO_list = new ArrayList<>();

            cartDTO.setId(c.getId());
            cartDTO.setStatus(c.getStatus());
            cartDTO.setTotal(c.getTotal());
            cartDTO.setIsCartLinkedToSale(c.getIsCartLinkedToSale());

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
    @CircuitBreaker(name="products-service",fallbackMethod="fallbackCart")
    @Retry(name="products-service")
    public CartDTO findCart(Long id) {

        Cart cart = cartRep.findById(id).orElse(null);
        CartDTO cartDTO = new CartDTO();

        if(cart!=null) {

            List<ProductDTO> productDTO_list = new ArrayList<>();

            cartDTO.setId(cart.getId());
            cartDTO.setStatus(cart.getStatus());
            cartDTO.setTotal(cart.getTotal());
            cartDTO.setIsCartLinkedToSale(cart.getIsCartLinkedToSale());

            for (Long i : cart.getProduct_list()) {

                ProductDTO productdto = productapi.findProduct(i);
                productDTO_list.add(productdto);

            }

            cartDTO.setProduct_list(productDTO_list);

        }

        return cartDTO;

    }

    @Override
    public List<Long> findCartProductsId(Long cart_id){
        return cartRep.findById(cart_id).orElse(null).getProduct_list();
    }

    @Override
    public String deleteCart(Long id) {

        Cart cart = cartRep.findById(id).orElse(null);

        if(cart.getIsCartLinkedToSale()){
            return "The cart cannot be deleted because it is linked to a sale";
        }

        if(cart!=null){
            cart.setStatus(false);
            cartRep.save(cart);
            return "Cart deleted successfully";
        }

        return "The cart doesn't exist";

    }

    @Override
    @CircuitBreaker(name="products-service",fallbackMethod="fallbackString")
    @Retry(name="products-service")
    public String saveCart(Cart cart) {

        Double total = 0.00;

        for (Long i : cart.getProduct_list()) {

            ProductDTO productdto = productapi.findProduct(i);

            if(productdto==null || !productdto.getStatus()){
                return "ERROR: The product " + i + " doesn't exist";
            }

            total += productdto.getPrice();

        }

        cart.setTotal(total);
        cart.setStatus(true);
        cart.setIsCartLinkedToSale(false);

        cartRep.save(cart);
        return "Cart saved successfully";
    }

    @Override
    @CircuitBreaker(name="products-service",fallbackMethod="fallbackString")
    @Retry(name="products-service")
    public String editCart(Cart cart, Long id) {

        Cart c = cartRep.findById(id).orElse(null);

        if (c != null) {

            if(!c.getIsCartLinkedToSale()) {

                cart.setId(id);

                Double total = 0.00;

                for (Long i : cart.getProduct_list()) {

                    ProductDTO productdto = productapi.findProduct(i);

                    if (productapi.findProduct(i) == null) {
                        return "ERROR: The product " + i + " doesn't exist";
                    }

                    total += productdto.getPrice();

                }

                cart.setTotal(total);
                cart.setStatus(c.getStatus());
                cart.setIsCartLinkedToSale(c.getIsCartLinkedToSale());

                cartRep.save(cart);
                return "Cart edited successfully";

            }

            return "The cart cannot be edited because it is linked to a sale";

        }

        return "The cart doesn't exist";

    }

    @Override
    @CircuitBreaker(name="products-service",fallbackMethod="fallbackString")
    @Retry(name="products-service")
    public String addProductCart(Long cart_id, Long product_id) {

        Cart cart = cartRep.findById(cart_id).orElse(null);

        if(cart==null){
            return "The cart doesn't exist";
        }

        if(!cart.getIsCartLinkedToSale()) {

            ProductDTO productdto = productapi.findProduct(product_id);

            if (productdto == null || !productdto.getStatus()) {
                return "ERROR: The product " + product_id + " doesn't exist";
            }

            List<Long> product_list = cart.getProduct_list();

            product_list.add(product_id);

            cart.setProduct_list(product_list);
            cart.setTotal(cart.getTotal() + productdto.getPrice());

            cartRep.save(cart);

            return "The product was added successfully";

        }

        return "The cart cannot be edited because it is linked to a sale";

    }

    @Override
    @CircuitBreaker(name="products-service",fallbackMethod="fallbackString")
    @Retry(name="products-service")
    public String deleteProductCart(Long cart_id, Long product_id) {

        Cart cart = cartRep.findById(cart_id).orElse(null);

        if(cart==null){
            return "The cart doesn't exist";
        }

        if(!cart.getIsCartLinkedToSale()) {

            ProductDTO productdto = productapi.findProduct(product_id);

            if (productdto == null || !productdto.getStatus()) {
                return "ERROR: The product " + product_id + " doesn't exist";
            }

            List<Long> product_list = cart.getProduct_list();

            if (product_list.remove(product_id)) {

                cart.setProduct_list(product_list);
                cart.setTotal(cart.getTotal() - productdto.getPrice());
                cartRep.save(cart);

                return "Product removed successfully";

            }

            return "The product doesn't exist in the cart selected";

        }

        return "The cart cannot be edited because it is linked to a sale";

    }

    @Override
    public void assignSale(Long cart_id, Long sale_id) {

        Cart cart = cartRep.findById(cart_id).orElse(null);

        if(cart==null){
            return;
        }

        cart.setIsCartLinkedToSale(true);

        cartRep.save(cart);

    }

    @Override
    public void unassignSale(Long cart_id) {
        Cart cart = cartRep.findById(cart_id).orElse(null);

        if(cart==null){
            return;
        }

        cart.setIsCartLinkedToSale(false);

        cartRep.save(cart);
    }

    public List<CartDTO> fallbackCartList(Throwable throwable){

        System.err.println("Error: " + throwable.getMessage());
        List<CartDTO> cart_list_error = new ArrayList<>();
        cart_list_error.add(cartError);

        return cart_list_error;

    }

    public CartDTO fallbackCart(Throwable throwable){

        System.err.println("Error: " + throwable.getMessage());

        return cartError;

    }

    public String fallbackString(Throwable throwable){

        System.err.println("Error: " + throwable.getMessage());

        return "Error: " + throwable.getMessage();

    }

}
