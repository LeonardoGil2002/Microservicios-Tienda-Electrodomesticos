package com.todocodeacademy.cart_service.model;

import com.todocodeacademy.cart_service.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    private List<Long> product_list;
    private Double total;
    private Boolean status;
    private Boolean isCartLinkedToSale;

}
