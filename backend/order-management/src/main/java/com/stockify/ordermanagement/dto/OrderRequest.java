package com.stockify.ordermanagement.dto;

import java.time.LocalDate;

public class OrderRequest {
    private int organisation;
    private int customerId;
    private int supplierId;
    private int invoiceId;
    private LocalDate orderDate;
    private LocalDate completionDate;

    public int getOrganisation() {
        return organisation;
    }

    public void setOrganisation(int organisation) {
        this.organisation = organisation;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
}
