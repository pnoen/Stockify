package com.stockify.invoiceservice;

import com.stockify.invoiceservice.controller.EmailController;
import com.stockify.invoiceservice.dto.ApiResponse;
import com.stockify.invoiceservice.dto.InvoiceIdRequest;
import com.stockify.invoiceservice.model.Invoice;
import com.stockify.invoiceservice.service.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EmailControllerTests {

	private AutoCloseable closeable;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private EmailController emailController;

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	void testSendEmailSuccess() throws IOException {
		InvoiceIdRequest request = new InvoiceIdRequest();
		request.setInvoiceId(123);

		Invoice invoice = new Invoice();
		when(emailService.getInvoiceById(123)).thenReturn(invoice);
		when(emailService.sendTextEmail(invoice)).thenReturn(true);

		ResponseEntity<ApiResponse> response = emailController.send(request);

		assertEquals(200, Objects.requireNonNull(response.getBody()).getStatusCode());
		assertEquals("Invoice emailed successfully.", response.getBody().getMessage());
	}

	@Test
	void testSendEmailFailure() throws IOException {
		InvoiceIdRequest request = new InvoiceIdRequest();
		request.setInvoiceId(123);

		Invoice invoice = new Invoice();
		when(emailService.getInvoiceById(123)).thenReturn(invoice);
		when(emailService.sendTextEmail(invoice)).thenReturn(false);

		ResponseEntity<ApiResponse> response = emailController.send(request);

		assertEquals(404, Objects.requireNonNull(response.getBody()).getStatusCode());
		assertEquals("Invoice could not be emailed.", response.getBody().getMessage());
	}
}
