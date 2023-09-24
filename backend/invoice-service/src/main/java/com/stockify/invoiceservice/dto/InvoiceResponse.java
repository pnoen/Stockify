package com.stockify.invoiceservice.dto;

import com.stockify.invoiceservice.model.Invoice;

public class InvoiceResponse {

    private Invoice invoice;
    private int statusCode;

    public InvoiceResponse(int statusCode, Invoice invoice) {
        this.invoice = invoice;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}