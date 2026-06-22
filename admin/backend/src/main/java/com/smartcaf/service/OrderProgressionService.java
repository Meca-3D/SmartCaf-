package com.smartcaf.service;

import com.smartcaf.config.DatabaseConnectionChecker;
import com.smartcaf.model.Order;
import com.smartcaf.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Progresse automatiquement les statuts de commande côté serveur,
 * indépendamment de la présence d'un utilisateur sur la page admin.
 *
 * Transitions :
 *   PENDING     → IN_PROGRESS  après 10 secondes depuis la création
 *   IN_PROGRESS → COMPLETED    après 30 secondes depuis la création
 *                  (toutes les 3 commandes finalisées → CANCELLED)
 */
@Service
public class OrderProgressionService {

    @Autowired
    private OrderRepository orderRepository;

    private final AtomicInteger finalCount = new AtomicInteger(0);

    @Scheduled(fixedDelay = 5000)
    public void progressOrders() {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        // PENDING → IN_PROGRESS après 10 s
        List<Order> pending = orderRepository.findByStatusAndCreatedAtBefore(
                "PENDING", now.minusSeconds(10));
        for (Order order : pending) {
            order.setStatus("IN_PROGRESS");
            orderRepository.save(order);
        }

        // IN_PROGRESS → COMPLETED / CANCELLED après 30 s au total
        List<Order> inProgress = orderRepository.findByStatusAndCreatedAtBefore(
                "IN_PROGRESS", now.minusSeconds(30));
        for (Order order : inProgress) {
            int count = finalCount.incrementAndGet();
            order.setStatus(count % 3 == 0 ? "CANCELLED" : "COMPLETED");
            orderRepository.save(order);
        }
    }
}
