package com.stockify.invoiceservice.service;

import com.sendgrid.*;
import com.stockify.invoiceservice.dto.*;
import com.stockify.invoiceservice.model.Invoice;
import com.stockify.invoiceservice.model.Order;
import com.stockify.invoiceservice.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final WebClient webClientUser = WebClient.create("http://localhost:8080");
    private final WebClient webClientOrder = WebClient.create("http://localhost:8081");
    private final WebClient webClientInvoice = WebClient.create("http://localhost:8082");
    public boolean sendTextEmail(Invoice invoice) throws IOException {

        // Get customer and supplier emails through their id in order details
        List<String> emails = getEmails(invoice);

        // The sender email should be the same as we used to Create a Single Sender Verification
        Email from = new Email("ljin7638@uni.sydney.edu.au");
        String subject = "Invoice for Completed Order - Stockify";
        Content content = new Content("text/plain", generateInvoiceContent(invoice.getOrderId()));

        // Create a Mail object without setting the 'to' field
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);

        // Create a Personalization object and add multiple recipients
        Personalization personalization = new Personalization();
        for (String email : emails) {
            personalization.addTo(new Email(email));
        }
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info(response.getBody());
            return true;
        } catch (IOException ex) {
            System.out.println();
            System.out.println("Exception is: " + ex);
            System.out.println();
            return false;
        }
    }

    public Order getOrder(Invoice invoice) {
        ResponseEntity<OrderResponse> responseEntity = webClientOrder.get()
                .uri("/order/getOrderById?orderId=" + invoice.getOrderId())
                .retrieve()
                .toEntity(OrderResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            OrderResponse orderResponse = responseEntity.getBody();

            if (orderResponse != null && orderResponse.getOrder() != null) {
                return orderResponse.getOrder();
            }
        }
        return null;
    }

    public List<String> getEmails(Invoice invoice) {
        Order order = getOrder(invoice);
        List<String> emails = new ArrayList<>(); // [0] is customer and [1] is supplier

        String customerId = String.valueOf(order.getCustomerId());
        String supplierId = String.valueOf(order.getSupplierId());

        emails.add(getEmail(customerId));
        emails.add(getEmail(supplierId));

        return emails;
    }

    public String getEmail(String userId) {
        ResponseEntity<UserEmailResponse> responseEntity = webClientUser.get()
                .uri("/account/getUserEmail?userId=" + userId)
                .retrieve()
                .toEntity(UserEmailResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            UserEmailResponse userEmailResponse = responseEntity.getBody();

            if (userEmailResponse != null) {
                return userEmailResponse.getEmail();
            }
        }
        return null;
    }

    public Invoice getInvoiceById(int invoiceId) {
        ResponseEntity<InvoiceResponse> responseEntity = webClientInvoice.get()
                .uri("/invoice/getInvoiceById?invoiceId=" + invoiceId)
                .retrieve()
                .toEntity(InvoiceResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            InvoiceResponse invoiceResponse = responseEntity.getBody();

            if (invoiceResponse != null && invoiceResponse.getInvoice() != null) {
                return invoiceResponse.getInvoice();
            }
        }
        return null;
    }

    public String generateInvoiceContent(int orderId) {

        Order order = getOrder(orderId);
        List<OrderItem> orderItemList = getOrderItem(orderId);

        if (order != null) {
            return generateInvoiceString(order, orderItemList);
        }

        return "failed";
    }

    public Order getOrder(int orderId) {
        ResponseEntity<OrderResponse> responseEntity = webClientOrder.get()
                .uri("/order/getOrderById?orderId=" + orderId)
                .retrieve()
                .toEntity(OrderResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            OrderResponse orderResponse = responseEntity.getBody();

            if (orderResponse != null && orderResponse.getOrder() != null) {
                Order order = orderResponse.getOrder();

                return order;
            }
        }

        return null;
    }

    public List<OrderItem> getOrderItem(int orderId) {
        ResponseEntity<OrderItemListResponse> responseEntity = webClientOrder.get()
                .uri("/orderItem/getAllByOrderId?orderId=" + orderId)
                .retrieve()
                .toEntity(OrderItemListResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            OrderItemListResponse orderItemListResponse = responseEntity.getBody();

            if (orderItemListResponse != null && orderItemListResponse.getOrderItem() != null) {
                List<OrderItem> orderItemList = orderItemListResponse.getOrderItem();

                return orderItemList;
            }
        }

        return null;
    }

    public String generateInvoiceString(Order order, List<OrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();

        // Line 1: Order ID
        sb.append("Order ID: ");
        sb.append(order.getId());
        sb.append("\n");

        // Line 2: Empty
        sb.append("\n");

        // Line 3: Customer ID
        sb.append("Customer ID: ");
        sb.append(order.getCustomerId());
        sb.append("\n");

        // Line 4: Supplier ID
        sb.append("Supplier ID: ");
        sb.append(order.getSupplierId());
        sb.append("\n");

        // Line 5: Organisation ID
        sb.append("Organisation ID: ");
        sb.append(order.getOrganisation());
        sb.append("\n");

        // Line 6: Empty
        sb.append("\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Line 7: Order date
        sb.append("Order Date: ");
        String orderDate = order.getOrderDate().format(formatter);
        sb.append(orderDate);
        sb.append("\n");

        // Line 8: Order completion date
        sb.append("Completion Date: ");
        String completionDate = order.getCompletionDate().format(formatter);
        sb.append(completionDate);
        sb.append("\n");

        // Line 9: Empty
        sb.append("\n");

        // Line 10: Total cost
        sb.append("Total cost: ");
        String totalCost = String.format("%.2f", order.getTotalCost());
        sb.append(totalCost);
        sb.append("\n");

        // Line 11: Empty
        sb.append("\n");

        // List of all items
        for (OrderItem o:orderItemList) {
            sb.append("Product ID: ");
            String itemId = String.valueOf(o.getId());
            sb.append(itemId);
            sb.append("     Product Quantity: ");
            String itemQuantity = String.format("%.2f", o.getQuantity());
            sb.append(itemQuantity);
            sb.append("     Product Price: ");
            String itemPrice = String.format("%.2f", o.getPrice() * o.getQuantity());
            sb.append(itemPrice);
            sb.append("\n");
        }

        sb.append("\n");

        // Final line: Total cost again
        sb.append("Total cost: ");
        sb.append(totalCost);
        sb.append("\n");

        return sb.toString();
    }
}