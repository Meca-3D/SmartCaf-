package com.smartcaf.repository;

import com.smartcaf.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Retourne les statistiques de vente par produit :
     * productId, productName, productCategory, totalQuantitySold, totalRevenue
     */
    @Query(value = """
            SELECT
                p.id              AS productId,
                p.name            AS productName,
                p.category        AS productCategory,
                SUM(oi.quantity)  AS totalQuantitySold,
                SUM(oi.price * oi.quantity) AS totalRevenue
            FROM order_items oi
            JOIN products p ON p.id = oi.product_id
            GROUP BY p.id, p.name, p.category
            ORDER BY SUM(oi.quantity) DESC
            """, nativeQuery = true)
    List<Object[]> findSalesStatsByProduct();
}
