package com.stockify.ordermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockify.ordermanagement.constants.OrderStatus;
import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.model.OrderItem;
import com.stockify.ordermanagement.model.ProductItem;
import com.stockify.ordermanagement.repository.OrderItemRepository;
import com.stockify.ordermanagement.repository.OrderRepository;
import com.stockify.ordermanagement.service.EmailService;
import com.stockify.ordermanagement.service.OrderService;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    private OrderService orderService = new OrderService();
    @Autowired
    private EmailService emailService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        int organisation = orderRequest.getOrganisation();
        int customerId = orderRequest.getCustomerId();
        int supplierId = orderRequest.getSupplierId();
        LocalDate orderDate = orderRequest.getOrderDate();
        LocalDate completionDate = orderRequest.getCompletionDate();

        Order newOrder = new Order();
        newOrder.setOrganisation(organisation);
        newOrder.setCustomerId(customerId);
        newOrder.setBusinessCode(supplierId);
        newOrder.setOrderDate(orderDate);
        newOrder.setCompletionDate(completionDate);

        orderRepository.save(newOrder);

        return ResponseEntity.ok(new ApiResponse(200, String.valueOf(newOrder.getId())));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteOrder(@RequestBody OrderIdRequest orderIdRequest) {
        Optional<Order> orderOptional = orderRepository.findById(orderIdRequest.getOrderId());

        if (orderOptional.isPresent()) {
            orderRepository.deleteById(orderIdRequest.getOrderId());
            return ResponseEntity.ok(new ApiResponse(200, "Order deleted successfully."));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(404, "Order does not exist."));
        }
    }

    // Get a list of all the orders
    @GetMapping("/getAllCurrentCustomerOrders")
    public ResponseEntity<?> getAllCurrentCustomerOrders(@RequestParam String email) {
        try {
            int customerId = orderService.getUserIdByEmail(email);
            if (customerId <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(400, "Invalid email or user not found."));
            }
            List<Order> ordersForCustomer = orderRepository.findByCustomerId(customerId);
            List<Order> filteredOrders = ordersForCustomer.stream()
                    .filter(o -> !OrderStatus.DRAFT.equals(o.getOrderStatus()) && !OrderStatus.COMPLETE.equals(o.getOrderStatus())  && !OrderStatus.CANCELLED.equals(o.getOrderStatus()))
                    .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new OrderListResponse(200, filteredOrders));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "An error occurred while processing the request."));
        }
    }

    @GetMapping("/getAllCompletedCustomerOrders")
    public ResponseEntity<?> getAllCompletedCustomerOrders(@RequestParam String email) {
        try {
            int customerId = orderService.getUserIdByEmail(email);
            if (customerId <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(400, "Invalid email or user not found."));
            }
            List<Order> ordersForCustomer = orderRepository.findByCustomerId(customerId);
            List<Order> completedOrders = ordersForCustomer.stream()
                    .filter(o -> OrderStatus.COMPLETE.equals(o.getOrderStatus()) || OrderStatus.CANCELLED.equals(o.getOrderStatus()))
                    .sorted(Comparator.comparing(Order::getCompletionDate).reversed())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new OrderListResponse(200, completedOrders));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "An error occurred while processing the request."));
        }
    }
    @GetMapping("/getAllCurrentBusinessOrders")
    public ResponseEntity<?> getAllCurrentBusinessOrders(@RequestParam String email) {
        try {
            int businessCode = orderService.getBusinessCodeByEmail(email);
            if (businessCode <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(400, "Invalid email or user not found."));
            }
            List<Order> ordersForCustomer = orderRepository.findByBusinessCode(businessCode);
            List<Order> filteredOrders = ordersForCustomer.stream()
                    .filter(o -> !OrderStatus.DRAFT.equals(o.getOrderStatus()) && !OrderStatus.COMPLETE.equals(o.getOrderStatus()) && !OrderStatus.CANCELLED.equals(o.getOrderStatus()))
                    .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new OrderListResponse(200, filteredOrders));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "An error occurred while processing the request."));
        }
    }

    @GetMapping("/getAllCompletedBusinessOrders")
    public ResponseEntity<?> getAllCompletedBusinessOrders(@RequestParam String email) {
        try {
            int businessCode = orderService.getBusinessCodeByEmail(email);
            if (businessCode <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse(400, "Invalid email or user not found."));
            }
            List<Order> ordersForCustomer = orderRepository.findByBusinessCode(businessCode);
            List<Order> completedOrders = ordersForCustomer.stream()
                    .filter(o -> OrderStatus.COMPLETE.equals(o.getOrderStatus()) || OrderStatus.CANCELLED.equals(o.getOrderStatus()))
                    .sorted(Comparator.comparing(Order::getCompletionDate).reversed())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new OrderListResponse(200, completedOrders));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "An error occurred while processing the request."));
        }
    }

    @GetMapping("/getBusinessStats")
    public ResponseEntity<Map<String, Integer>> getBusinessStats(@RequestParam String email) {
        try {
            int businessCode = orderService.getBusinessCodeByEmail(email);
            if (businessCode <= 0) {
                return ResponseEntity.badRequest().body(null);
            }

            Map<String, Integer> stats = new HashMap<>();

            if (orderRepository.countByBusinessCode(businessCode) == 0) {
                stats.put("New Orders", 0);
                stats.put("Completed Orders", 0);
                stats.put("Total Revenue ($)", 0);
                stats.put("Orders Awaiting Shipment", 0);
                stats.put("Orders Cancelled", 0);
                stats.put("Total Orders", 0);
                return ResponseEntity.ok(stats);
            }

            stats.put("New Orders", orderRepository.countByBusinessCodeAndOrderStatus(businessCode, OrderStatus.PURCHASED));
            stats.put("Completed Orders", orderRepository.countByBusinessCodeAndOrderStatus(businessCode, OrderStatus.COMPLETE));
            stats.put("Total Revenue ($)", orderRepository.sumTotalCostByBusinessCode(businessCode));
            stats.put("Orders Awaiting Shipment", orderRepository.countByBusinessCodeAndOrderStatus(businessCode, OrderStatus.AWAITING_SHIPMENT));
            stats.put("Orders Cancelled", orderRepository.countByBusinessCodeAndOrderStatus(businessCode, OrderStatus.CANCELLED));
            stats.put("Total Orders", orderRepository.countByBusinessCode(businessCode));

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Get an order by its ID
    @GetMapping("/getOrderById")
    public ResponseEntity<OrderResponse> getOrderById(@RequestParam int orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setBusinessName(orderService.getBusinessNameByBusinessCode(order.getBusinessCode()));
            return ResponseEntity.ok(new OrderResponse(200, order));
        } else {
            return ResponseEntity.ok(new OrderResponse(404, null));
        }
    }

    // Update Total Cost
    @PostMapping("/updateTotalCost")
    public ResponseEntity<ApiResponse> updateTotalCost(@RequestBody OrderCostUpdateRequest orderCostUpdateRequest) {
        Optional<Order> orderOptional = orderRepository.findById(orderCostUpdateRequest.getOrderId());

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            double newTotalCost = Double.parseDouble(String.format("%.2f", (order.getTotalCost() + orderCostUpdateRequest.getPrice())));
            order.setTotalCost(newTotalCost);
            orderRepository.save(order);

            return ResponseEntity.ok(new ApiResponse(200, "Order total cost edited successfully."));
        } else {
            return ResponseEntity.ok(new ApiResponse(404, "Order does not exist"));
        }
    }
    
    @PostMapping("setInvoiceIdToOrder")
    public ResponseEntity<BooleanResponse> setInvoiceIdToOrder(@RequestBody InvoiceIdRequest invoiceIdRequest) {
        Optional<Order> orderOptional = orderRepository.findById(invoiceIdRequest.getOrderId());

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            order.setInvoiceId(invoiceIdRequest.getInvoiceId());

            orderRepository.save(order);

            return ResponseEntity.ok(new BooleanResponse(200, true));
        } else {
            return ResponseEntity.ok(new BooleanResponse(404, false));
        }
    }
    @PostMapping("/createDraftOrder")
    public ResponseEntity<ApiResponse> createDraftOrder(@RequestParam String email) {
        int customerId = orderService.getUserIdByEmail(email);
        Optional<Order> draftOrderOptional = orderRepository.findByOrderStatusAndCustomerId(OrderStatus.DRAFT, customerId);

        Order draftOrder;
        if (!draftOrderOptional.isPresent()) {
            draftOrder = new Order();
            draftOrder.setOrderStatus(OrderStatus.DRAFT);
            draftOrder.setCustomerId(customerId);
            orderRepository.save(draftOrder);
        } else {
            draftOrder = draftOrderOptional.get();
        }
        return ResponseEntity.ok(new ApiResponse(200, String.valueOf(draftOrder.getId())));
    }
    @GetMapping("/getDraftOrder")
    public ResponseEntity<ApiResponse> getDraftOrder(@RequestParam String email) {
        int customerId = orderService.getUserIdByEmail(email);
        Optional<Order> draftOrderOptional = orderRepository.findByOrderStatusAndCustomerId(OrderStatus.DRAFT, customerId);
        Order draftOrder;
        if (!draftOrderOptional.isPresent()) {
            return ResponseEntity.accepted().body(new ApiResponse(202, "Shopping Cart is Empty"));
        } else {
            draftOrder = draftOrderOptional.get();
        }
        return ResponseEntity.ok(new ApiResponse(200, String.valueOf(draftOrder.getId())));
    }

    @PostMapping("/updateDraftOrder")
    public ResponseEntity<ApiResponse> updateDraftOrder(@RequestBody UpdateDraftOrderRequest updateDraftOrderRequest) {
        Optional<Order> draftOrderOptional = orderRepository.findById(updateDraftOrderRequest.getOrderId());

        if (!draftOrderOptional.isPresent()) {
            return ResponseEntity.accepted().body(new ApiResponse(202, "Cannot place order with empty shopping cart."));
        }

        Order draftOrder = draftOrderOptional.get();
        draftOrder.setOrderStatus(OrderStatus.PURCHASED);
        draftOrder.setTotalCost(updateDraftOrderRequest.getTotalCost());
        draftOrder.setOrderDate(LocalDate.now());
        draftOrder.setBusinessName(orderService.getBusinessNameByBusinessCode(draftOrder.getBusinessCode()));
        orderRepository.save(draftOrder);

        return ResponseEntity.ok(new ApiResponse(200, "Order successfully placed."));
    }

    @PostMapping("/updateOrderStatus")
    public ResponseEntity<ApiResponse> updateOrderStatus(@RequestBody UpdateOrderStatusRequest updateOrderStatusRequest) {
        Optional<Order> orderOptional = orderRepository.findById(updateOrderStatusRequest.getOrderId());

        if (orderOptional.isEmpty()) {
            return ResponseEntity.accepted().body(new ApiResponse(202, "Cannot find order."));
        }
        Order order = orderOptional.get();
        if (updateOrderStatusRequest.getOrderStatus() == OrderStatus.COMPLETE || updateOrderStatusRequest.getOrderStatus() == OrderStatus.CANCELLED ){
            order.setCompletionDate(LocalDate.now());
        }
        if(updateOrderStatusRequest.getOrderStatus() == OrderStatus.COMPLETE) {
            List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());
            List<ProductItem> productItems = fetchProductDetails(orderItems);

            String emailContent = generateEmailContent(order, productItems);
            String customerEmail = orderService.getUserEmailByCustomerId(order.getCustomerId());
            String emailSubject = "Stockify - Order #" + updateOrderStatusRequest.getOrderId() + " Invoice";
            emailService.sendEmail(customerEmail, emailSubject, emailContent);
        }
        order.setOrderStatus(updateOrderStatusRequest.getOrderStatus());
        orderRepository.save(order);
        return ResponseEntity.ok(new ApiResponse(200, "Order status successfully updated to " + order.getOrderStatus()));
    }






    private List<ProductItem> fetchProductDetails(List<OrderItem> orderItems) {
        List<Integer> productIds = orderItems.stream()
                .map(OrderItem::getProductId)
                .collect(Collectors.toList());

        String productUrl = "http://localhost:8083/api/product/getProducts?ids="
                + productIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        ProductItemListResponse fetchedProductItemListResponse = new RestTemplate().getForObject(productUrl, ProductItemListResponse.class);

        if (fetchedProductItemListResponse != null) {
            List<ProductItem> productItems = fetchedProductItemListResponse.getProducts();
            for (ProductItem product : productItems) {
                for (OrderItem orderItem : orderItems) {
                    if (orderItem.getProductId() == product.getId()) {
                        product.setQuantity(orderItem.getQuantity());
                        product.setOrderItemId(orderItem.getId());
                        break;
                    }
                }
            }
            return productItems;
        } else {
            return new ArrayList<>();
        }
    }

    private String generateEmailContent(Order order, List<ProductItem> productItems) {
        StringBuilder emailContent = new StringBuilder();

        emailContent.append("<html><head>")
                .append("<style>")
                .append("body { color: #000; }")
                .append("table {width: 100%; border-collapse: collapse; margin-bottom: 20px;}")
                .append("th, td {border: 1px solid #ddd; text-align: left; padding: 8px;}")
                .append("th {background-color: #cbf5d6;}")
                .append("div.emailBody {padding: 0 1em;}")
                .append("h1 {text-align: center; margin: 0;}")
                .append("hr {border-style: solid; border-color: #00000050;}")
                .append("p.orderItems {margin-bottom: 0.2em;}")
                .append("p.subtotal {margin-top: 0.5em; text-align: right}")
                .append("</style>")
                .append("</head><body>");

        emailContent
                .append("<hr />")
                .append("<h1>Order #").append(order.getId()).append("</h1>")
                .append("<hr />")
                .append("<div class='emailBody'>")
                .append("<p><b>Business Name: </b>").append(order.getBusinessName()).append("</p>")
                .append("<p><b>Order Date: </b>").append(order.getOrderDate()).append("</p>")
                .append("<p><b>Completion Date: </b>").append(order.getCompletionDate()).append("</p>");

        emailContent.append("<p class='orderItems'><b>Order Items:</b></p>")
                .append("<table>")
                .append("<tr><th>Item Name</th><th>Description</th><th>Price ($)</th><th>Quantity</th></tr>");

        float subtotal = 0;
        for (ProductItem productItem : productItems) {
            emailContent.append("<tr>")
                    .append("<td>").append(productItem.getName()).append("</td>")
                    .append("<td>").append(productItem.getDescription()).append("</td>")
                    .append("<td>").append(productItem.getPrice()).append("</td>")
                    .append("<td>").append(productItem.getQuantity()).append("</td>")
                    .append("</tr>");
            subtotal += productItem.getPrice();
        }

        emailContent.append("</table>");

        emailContent.append("<p class='subtotal'>Subtotal (").append(productItems.size()).append(" items): $")
                .append(subtotal).append("</p>")
                .append("<p>Thank you for your business!</p>")
                .append("</div>")
                .append("</body></html>");

        return emailContent.toString();
    }
}