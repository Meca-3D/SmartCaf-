package com.smartcaf.dto;

import java.util.List;

public class CreateOrderRequest {
    private String customerName;
    private String orderType;   // "ON_SITE" or "CLICK_AND_COLLECT"
    private Long tableId;       // null for click & collect
    private List<CreateOrderItemRequest> items;

    public CreateOrderRequest() {}

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public Long getTableId() { return tableId; }
    public void setTableId(Long tableId) { this.tableId = tableId; }

    public List<CreateOrderItemRequest> getItems() { return items; }
    public void setItems(List<CreateOrderItemRequest> items) { this.items = items; }
}
