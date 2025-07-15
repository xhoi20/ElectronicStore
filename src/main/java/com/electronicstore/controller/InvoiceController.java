package com.electronicstore.controller;


import com.electronicstore.dto.PurchaseItemRequest;
import com.electronicstore.entity.Invoice;
import com.electronicstore.entity.PurchaseItem;

import com.electronicstore.repository.ItemRepository;

import com.electronicstore.repository.PurchaseRepository;
import com.electronicstore.service.InvoiceService.SalesMetrics;
import com.electronicstore.service.serviceInterface.IInvoiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/invoices")
public class InvoiceController extends BaseController{

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
    @GetMapping("/cashier/{userId}")
    public ResponseEntity<Map<String, Object>> getInvoicesByCashier(@PathVariable Long userId) {
        try {
            checkManagerRole();
            List<Invoice> invoices = invoiceService.getInvoicesByCashier(userId);
            return createSuccessResponse(invoices, "Faturat për arketar u gjeten me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/daily-total/{userId}")
    public ResponseEntity<Map<String, Object>> getDailyTotalByCashier(@PathVariable Long userId) {
        try {
            checkManagerRole();
            double total = invoiceService.getDailyTotalByCashier(userId);
            return createSuccessResponse(total, "Totali ditor per arketar u llogarit me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @GetMapping("/sector/{userId}")
    public ResponseEntity<Map<String, Object>> getInvoicesBySector(@PathVariable Long userId) {
        try {
            checkManagerRole();
            List<Invoice> invoices = invoiceService.getInvoicesBySector(userId);
            return createSuccessResponse(invoices, "Faturat per sektor u gjetën me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/sales-metrics/{userId}")
    public ResponseEntity<Map<String, Object>> getSalesMetrics(@PathVariable Long userId) {
        try {
            checkManagerRole();
            SalesMetrics metrics = invoiceService.getSalesMetrics(userId);
            return createSuccessResponse(metrics, "Metrics e shitjeve u gjeten me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Map<String, Object>> deleteInvoice( @PathVariable Long invoiceId) {
        try {
            checkManagerRole();
            invoiceService.deleteInvoice( invoiceId);
            return createSuccessResponse(null, "Fatura u fshi me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
}
