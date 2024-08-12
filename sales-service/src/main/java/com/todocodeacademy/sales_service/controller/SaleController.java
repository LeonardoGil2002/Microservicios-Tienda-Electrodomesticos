package com.todocodeacademy.sales_service.controller;

import com.todocodeacademy.sales_service.dto.SaleDTO;
import com.todocodeacademy.sales_service.model.Sale;
import com.todocodeacademy.sales_service.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private ISaleService salSer;

    @GetMapping("/get")
    public List<SaleDTO> getSales(){
        return salSer.getSales();
    }

    @GetMapping("/get/{id}")
    public SaleDTO findSale(@PathVariable Long id){
        return salSer.findSale(id);
    }

    @PostMapping("/save")
    public String saveSale(@RequestBody Sale sale){
        return salSer.saveSale(sale);
    }

    @PutMapping("/edit/{id}")
    public String editSale(@RequestBody Sale sale, @PathVariable Long id){
        return salSer.editSale(sale, id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSale(@PathVariable Long id){
        return salSer.deleteSale(id);
    }

}
