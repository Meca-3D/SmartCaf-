package com.smartcaf.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardStatsDto(
        long totalProducts,
        long activeProducts,
        long totalOrders,
        long pendingOrders,
        long completedOrders,
        BigDecimal totalRevenue,
        List<ProductSalesDto> topProducts
) {}
