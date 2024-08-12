package com.todocodeacademy.sales_service.repository;

import com.todocodeacademy.sales_service.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ISaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s WHERE s.status = true")
    List<Sale> getEnabledSales();

}
