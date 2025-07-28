package com.electronicstore.service.serviceInterface;

import com.electronicstore.dto.PurchaseItemRequest;
import com.electronicstore.entity.Invoice;
import com.electronicstore.entity.InvoiceStatus;
import com.electronicstore.entity.PurchaseItem;
import com.electronicstore.service.InvoiceService;


import java.util.List;
import java.util.Optional;


public interface IInvoiceService {


    public Invoice createInvoice(Long userId, List<PurchaseItemRequest> purchaseItemRequests) throws Exception;

    Invoice changeStatus(Long id, InvoiceStatus status);
    List<Invoice> getInvoicesByCashier(Long userId);


    double getDailyTotalByCashier(Long userId);


   List<Invoice> getInvoicesBySector(Long userId)throws Exception;
Optional<Invoice> getInvoiceById(Long invoiceId);
Iterable<Invoice>getAllInvoices();
    InvoiceService.SalesMetrics getSalesMetrics(Long userId)throws Exception;
public void deleteInvoice(Long invoiceId)throws Exception;
}