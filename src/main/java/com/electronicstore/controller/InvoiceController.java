package com.electronicstore.controller;


import com.electronicstore.dto.PurchaseItemRequest;
import com.electronicstore.entity.Invoice;
import com.electronicstore.entity.PurchaseItem;

import com.electronicstore.repository.ItemRepository;

import com.electronicstore.repository.PurchaseRepository;
import com.electronicstore.service.InvoiceService.SalesMetrics;
import com.electronicstore.service.serviceInterface.IInvoiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private IInvoiceService invoiceService;
@Autowired
private ItemRepository itemRepository;
@Autowired
private PurchaseRepository purchaseRepository;

@PostMapping("/create")
public ResponseEntity<?> createInvoice(
        @RequestParam Long cashierId,
        @RequestBody List<PurchaseItemRequest> purchaseItemRequests) {
    try {
        List<PurchaseItem> purchaseItems = new ArrayList<>();
        for (PurchaseItemRequest req : purchaseItemRequests) {
            PurchaseItem item = new PurchaseItem();
            item.setItem(itemRepository.findById(req.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Item not found with ID: " + req.getItemId())));
            item.setPurchase(purchaseRepository.findById(req.getPurchaseId())
                    .orElseThrow(() -> new IllegalArgumentException("Purchase not found with ID: " + req.getPurchaseId())));
          item.setQuantity(req.getQuantity());

            purchaseItems.add(item);
        }

        Invoice invoice = invoiceService.createInvoice(cashierId, purchaseItems);
        return ResponseEntity.ok(invoice);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("An unexpected error occurred: " + e.getMessage()));
    }
}


    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<Invoice>> getInvoicesByCashier(@PathVariable Long userId) {
        List<Invoice> invoices = invoiceService.getInvoicesByCashier(userId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/cashier/{cashierId}/daily-total")
    public ResponseEntity<Double> getDailyTotalByCashier(@PathVariable Long userId) {
        double total = invoiceService.getDailyTotalByCashier(userId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Invoice>> getInvoicesByManager(@PathVariable Long userId) {
        try {
            List<Invoice> invoices = invoiceService.getInvoicesBySector(userId);
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/manager/{managerId}/metrics")
    public ResponseEntity<SalesMetrics> getSalesMetrics(@PathVariable Long userId) {
        try {
            SalesMetrics metrics = invoiceService.getSalesMetrics(userId);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/manager/{managerId}/delete/{invoiceId}")
    public ResponseEntity<String> deleteInvoice(
            @PathVariable Long userId,
            @PathVariable Long invoiceId) {
        try {
            invoiceService.deleteInvoice(userId, invoiceId);
            return ResponseEntity.ok("Fatura u fshi me sukses!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        }
    }
}
