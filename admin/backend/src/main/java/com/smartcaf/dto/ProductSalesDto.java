package com.smartcaf.dto;

import java.math.BigDecimal;

public record ProductSalesDto(
        Long productId,
        String productName,
        String productCategory,
        Long totalQuantitySold,
        BigDecimal totalRevenue
) {}
