package com.smartcaf.controller;

import com.smartcaf.config.DatabaseConnectionChecker;
import com.smartcaf.dto.DashboardStatsDto;
import com.smartcaf.dto.ProductSalesDto;
import com.smartcaf.model.Category;
import com.smartcaf.model.Order;
import com.smartcaf.model.Product;
import com.smartcaf.repository.CategoryRepository;
import com.smartcaf.repository.OrderItemRepository;
import com.smartcaf.repository.OrderRepository;
import com.smartcaf.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CategoryRepository categoryRepository;

    public AdminController(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.categoryRepository = categoryRepository;
    }

    // ===== DASHBOARD =====

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        long totalProducts = productRepository.count();
        long activeProducts = productRepository.countByIsActive(true);
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus("PENDING");
        long completedOrders = orderRepository.countByStatus("COMPLETED");
        BigDecimal totalRevenue = orderRepository.sumTotalRevenue();

        List<Object[]> rawSales = orderItemRepository.findSalesStatsByProduct();
        List<ProductSalesDto> topProducts = rawSales.stream()
                .limit(10)
                .map(row -> new ProductSalesDto(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        ((Number) row[3]).longValue(),
                        row[4] != null ? new BigDecimal(row[4].toString()) : BigDecimal.ZERO
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new DashboardStatsDto(
                totalProducts,
                activeProducts,
                totalOrders,
                pendingOrders,
                completedOrders,
                totalRevenue != null ? totalRevenue : BigDecimal.ZERO,
                topProducts
        ));
    }

    // ===== ORDERS =====

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.ok(orderRepository.findAllOrderedByDate());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        String newStatus = body.get("status");
        if (newStatus == null || newStatus.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(newStatus);
                    return ResponseEntity.ok(orderRepository.save(order));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== SALES STATS =====

    @GetMapping("/sales/by-product")
    public ResponseEntity<List<ProductSalesDto>> getSalesByProduct() {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        List<Object[]> rawSales = orderItemRepository.findSalesStatsByProduct();
        List<ProductSalesDto> sales = rawSales.stream()
                .map(row -> new ProductSalesDto(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        ((Number) row[3]).longValue(),
                        row[4] != null ? new BigDecimal(row[4].toString()) : BigDecimal.ZERO
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(sales);
    }

    // ===== PRODUCTS (admin) =====

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProductsAdmin() {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.ok(productRepository.findAll());
    }

    @PutMapping("/products/{id}/toggle-status")
    public ResponseEntity<Product> toggleProductStatus(@PathVariable Long id) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return productRepository.findById(id)
                .map(product -> {
                    product.setIsActive(Boolean.FALSE.equals(product.getIsActive()));
                    return ResponseEntity.ok(productRepository.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== CATEGORIES =====

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(category));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody Category updated) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return categoryRepository.findById(id)
                .map(cat -> {
                    cat.setName(updated.getName());
                    cat.setDescription(updated.getDescription());
                    return ResponseEntity.ok(categoryRepository.save(cat));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!DatabaseConnectionChecker.databaseConnected) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return categoryRepository.findById(id)
                .map(cat -> {
                    categoryRepository.delete(cat);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().<Void>build());
    }
}
