package com.todocodeacademy.sales_service.service;

import com.todocodeacademy.sales_service.dto.SaleDTO;
import com.todocodeacademy.sales_service.model.Sale;

import java.util.List;

public interface ISaleService {
    public List<SaleDTO> getSales();
    public SaleDTO findSale(Long id);
    public String saveSale(Sale sale);
    public String editSale(Sale sale, Long id);
    public String deleteSale(Long id);
}
