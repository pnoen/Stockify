package com.stockify.invoiceservice;

import com.stockify.invoiceservice.controller.InvoiceController;
import com.stockify.invoiceservice.dto.ApiResponse;
import com.stockify.invoiceservice.dto.InvoiceIdRequest;
import com.stockify.invoiceservice.dto.InvoiceRequest;
import com.stockify.invoiceservice.dto.InvoiceResponse;
import com.stockify.invoiceservice.model.Invoice;
import com.stockify.invoiceservice.repository.InvoiceRepository;
import com.stockify.invoiceservice.service.InvoiceService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceControllerTests {

	private AutoCloseable closeable;

	@Mock
	private InvoiceRepository invoiceRepository;

	@Mock
	private InvoiceService invoiceService;

	@InjectMocks
	private InvoiceController invoiceController;

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	void testCreateInvoiceFailure() {
		// If the invoice already exists in the database

		InvoiceRequest request = new InvoiceRequest();
		request.setOrderId(123);

		Invoice existingInvoice = new Invoice();
		when(invoiceRepository.findByOrderId(123)).thenReturn(Optional.of(existingInvoice));

		ResponseEntity<ApiResponse> response = invoiceController.createInvoice(request);

		assertEquals(404, Objects.requireNonNull(response.getBody()).getStatusCode());
		assertEquals("Invoice already exists for the order.", response.getBody().getMessage());
	}

	@Test
	void testDeleteInvoiceSuccess() {
		InvoiceIdRequest request = new InvoiceIdRequest();
		request.setInvoiceId(123);

		Invoice existingInvoice = new Invoice();
		when(invoiceRepository.findById(123)).thenReturn(Optional.of(existingInvoice));

		ResponseEntity<ApiResponse> response = invoiceController.deleteInvoice(request);

		assertEquals(200, Objects.requireNonNull(response.getBody()).getStatusCode());
		assertEquals("Invoice deleted successfully.", response.getBody().getMessage());
		verify(invoiceRepository).deleteById(123);
	}

	@Test
	void testDeleteInvoiceFailure() {
		InvoiceIdRequest request = new InvoiceIdRequest();
		request.setInvoiceId(123);

		when(invoiceRepository.findById(123)).thenReturn(Optional.empty());

		ResponseEntity<ApiResponse> response = invoiceController.deleteInvoice(request);

		assertEquals(404, Objects.requireNonNull(response.getBody()).getStatusCode());
		assertEquals("Invoice does not exist.", response.getBody().getMessage());
	}

	@Test
	void testGetInvoiceByIdSuccess() {
		int invoiceId = 123;

		Invoice existingInvoice = new Invoice();
		when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));

		ResponseEntity<InvoiceResponse> response = invoiceController.getInvoiceById(invoiceId);

		assertEquals(200, Objects.requireNonNull(response.getBody()).getStatusCode());
		assertNotNull(response.getBody().getInvoice());
		assertEquals(existingInvoice, response.getBody().getInvoice());
	}

	@Test
	void testGetInvoiceByIdFailure() {
		int invoiceId = 123;

		when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

		ResponseEntity<InvoiceResponse> response = invoiceController.getInvoiceById(invoiceId);

		assertEquals(404, Objects.requireNonNull(response.getBody()).getStatusCode());
		assertNull(response.getBody().getInvoice());
	}
}