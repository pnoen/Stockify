package com.stockify.invoiceservice.dto;

public class InvoiceIdInOrderRequest {
    private int invoiceId;
    private int orderId;

    public InvoiceIdInOrderRequest(int invoiceId, int orderId) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}