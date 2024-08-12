package com.todocodeacademy.sales_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cart_id;
    @Temporal(TemporalType.DATE)
    private LocalDate date;
    @ElementCollection
    private List<Long> product_list;
    private Double total;
    private Boolean status;

}
