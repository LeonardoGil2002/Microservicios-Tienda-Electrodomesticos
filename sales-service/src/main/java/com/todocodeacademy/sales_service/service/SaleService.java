package com.todocodeacademy.sales_service.service;

import com.todocodeacademy.sales_service.dto.CartDTO;
import com.todocodeacademy.sales_service.dto.SaleDTO;
import com.todocodeacademy.sales_service.model.Sale;
import com.todocodeacademy.sales_service.repository.CartAPIClient;
import com.todocodeacademy.sales_service.repository.ISaleRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService implements ISaleService{

    @Autowired
    private ISaleRepository salRep;

    @Autowired
    private CartAPIClient cartapi;

    private final SaleDTO saleError = new SaleDTO(0L, null, null, null, null, null);

    @Override
    @CircuitBreaker(name="cart-service", fallbackMethod = "fallbackSaleList")
    @Retry(name="cart-service")
    public List<SaleDTO> getSales() {

        List<Sale> sale_list = salRep.getEnabledSales();
        List<SaleDTO> saleDTO_list = new ArrayList<>();

        for(Sale s : sale_list){

            SaleDTO saleDTO = new SaleDTO();
            CartDTO cartDTO = cartapi.findCart(s.getCart_id());

            if(cartDTO.getId()==0){
                throw new RuntimeException("Runtime Exception");
            }

            saleDTO.setId(s.getId());
            saleDTO.setDate(s.getDate());
            saleDTO.setStatus(s.getStatus());
            saleDTO.setCart_id(s.getCart_id());
            saleDTO.setTotal(s.getTotal());
            saleDTO.setProduct_list(cartDTO.getProduct_list());

            saleDTO_list.add(saleDTO);

        }


        return saleDTO_list;
    }

    @Override
    @CircuitBreaker(name="cart-service", fallbackMethod = "fallbackSale")
    @Retry(name="cart-service")
    public SaleDTO findSale(Long id) {

        Sale s = salRep.findById(id).orElse(null);

        if(s!=null){
            SaleDTO saleDTO = new SaleDTO();
            CartDTO cartDTO = cartapi.findCart(s.getCart_id());

            if(cartDTO.getId()==0){
                throw new RuntimeException("Runtime Exception");
            }

            saleDTO.setId(s.getId());
            saleDTO.setDate(s.getDate());
            saleDTO.setStatus(s.getStatus());
            saleDTO.setCart_id(s.getCart_id());
            saleDTO.setTotal(s.getTotal());
            saleDTO.setProduct_list(cartDTO.getProduct_list());

            return saleDTO;

        }

        return null;
    }

    @Override
    @CircuitBreaker(name="cart-service", fallbackMethod = "fallbackString")
    @Retry(name="cart-service")
    public String saveSale(Sale sale) {

        CartDTO cart = cartapi.findCart(sale.getCart_id());

        if(cart.getId()==0){
            throw new RuntimeException("Runtime Exception");
        }

        if(cart==null || !cart.getStatus()){
            return "The cart assigned doesn't exist";
        }

        if(cart.getIsCartLinkedToSale()){
            return "The cart was already assigned to other sale";
        }

        sale.setTotal(cart.getTotal());
        sale.setProduct_list(cartapi.findCartProductsId(sale.getCart_id()));
        sale.setStatus(true);

        salRep.save(sale);

        cartapi.assignSale(sale.getCart_id(), sale.getId());

        return "Sale saved successfully";
    }

    @Override
    @CircuitBreaker(name="cart-service", fallbackMethod = "fallbackString")
    @Retry(name="cart-service")
    public String editSale(Sale sale, Long id) {

        CartDTO cart = cartapi.findCart(sale.getCart_id());

        Sale sal = salRep.findById(id).orElse(null);

        if(cart.getId()==0){
            throw new RuntimeException("Runtime Exception");
        }

        if(sal==null){
            return "The sale doesn't exist";
        }

        if(cart==null || !cart.getStatus()){
            return "The cart assigned doesn't exist";
        }

        if(cart.getIsCartLinkedToSale()){
            return "The cart was already assigned to other sale";
        }

        cartapi.unassignSale(sal.getCart_id());

        sal.setId(id);
        sal.setDate(sale.getDate());
        sal.setTotal(cart.getTotal());
        sal.setCart_id(sale.getCart_id());
        sal.setProduct_list(cartapi.findCartProductsId(cart.getId()));
        sal.setStatus(sale.getStatus());

        cartapi.assignSale(sale.getCart_id(), id);

        salRep.save(sal);


        return "Sale edited successfully";

    }

    @Override
    public String deleteSale(Long id) {

        Sale sal = salRep.findById(id).orElse(null);

        if(sal==null){
            return "The sale doesn't exist";
        }

        sal.setStatus(false);
        salRep.save(sal);

        return "Sale deleted successfully";
    }

    public List<SaleDTO> fallbackSaleList(Throwable throwable){

        System.err.println("Error: " + throwable.getMessage());

        List<SaleDTO> sale_error_list = new ArrayList<>();
        sale_error_list.add(saleError);
        return sale_error_list;

    }

    public SaleDTO fallbackSale(Throwable throwable){
        System.err.println("Error: " + throwable.getMessage());
        return saleError;
    }

    public String fallbackString(Throwable throwable){
        System.err.println("Error: " + throwable.getMessage());
        return "Error: " + throwable.getMessage();
    }

}
