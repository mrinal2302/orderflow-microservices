package com.inventryService.repository;

import com.inventryService.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity , Long> {
    List<OrderEntity> findByProductNameStartingWithIgnoringCase(String name);

}
