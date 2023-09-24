package com.stockify.invoiceservice.model;

import jakarta.persistence.*;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private int orderId;
    private String invoiceContent;

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceContent() { return invoiceContent; }

    public void setInvoiceContent(String invoiceContent) { this.invoiceContent = invoiceContent; }
}