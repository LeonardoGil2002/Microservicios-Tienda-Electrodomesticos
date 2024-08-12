package com.todocodeacademy.sales_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Long id;
    private Long cart_id;
    private LocalDate date;
    private List<ProductDTO> product_list;
    private Double total;
    private Boolean status;
}
