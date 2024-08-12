package com.todocodeacademy.sales_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private List<ProductDTO> product_list;
    private Double total;
    private Boolean status;
    private Boolean isCartLinkedToSale;
}
