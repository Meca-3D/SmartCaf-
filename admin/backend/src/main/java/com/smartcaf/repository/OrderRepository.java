package com.smartcaf.repository;

import com.smartcaf.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders ORDER BY created_at DESC", nativeQuery = true)
    List<Order> findAllOrderedByDate();

    List<Order> findByStatus(String status);

    long countByStatus(String status);

    @Query(value = "SELECT COALESCE(SUM(total_price), 0) FROM orders WHERE status = 'COMPLETED'", nativeQuery = true)
    BigDecimal sumTotalRevenue();

    @Query(value = "SELECT COALESCE(SUM(total_price), 0) FROM orders WHERE status = 'COMPLETED' AND DATE(created_at) = CURDATE()", nativeQuery = true)
    BigDecimal sumRevenuToday();
}
