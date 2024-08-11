package com.todocodeacademy.cart_service.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long id;
    private List<ProductDTO> product_list;
    private Double total;
    private Boolean status;
}
