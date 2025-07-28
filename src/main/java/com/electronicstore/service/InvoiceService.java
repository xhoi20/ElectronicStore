package com.electronicstore.service;

import com.electronicstore.dto.PurchaseItemRequest;
import com.electronicstore.entity.*;
import com.electronicstore.repository.InvoiceRepository;
import com.electronicstore.repository.ItemRepository;
import com.electronicstore.repository.PurchaseRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.serviceInterface.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService extends BaseService implements IInvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

@Transactional
public Invoice createInvoice(Long userId, List<PurchaseItemRequest> purchaseItemRequests) throws Exception {
    User cashier = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("Arketari nuk u gjet me ID: " + userId));

    if (cashier.getRole() != UserRole.CASHIER) {
        throw new Exception("Perdoruesi nuk është arketar");
    }
    List<PurchaseItem> purchaseItems = new ArrayList<>();
    for (PurchaseItemRequest req : purchaseItemRequests) {
        PurchaseItem item = new PurchaseItem();
        item.setItem(itemRepository.findById(req.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Artikulli nuk u gjet me ID: " + req.getItemId())));
        item.setPurchase(purchaseRepository.findById(req.getPurchaseId())
                .orElseThrow(() -> new IllegalArgumentException("Purchase nuk u gjet me ID: " + req.getPurchaseId())));
        item.setQuantity(req.getQuantity());
        purchaseItems.add(item);

    }
    Invoice invoice = new Invoice();
    invoice.setArketar(cashier);
    invoice.setArtikujt(purchaseItems);

    double total = 0.0;
    for (PurchaseItem item : purchaseItems) {

        if (item.getPurchase() == null) {
            throw new Exception("Purchase nuk mund të jete null për artikullin");
        }
        Purchase purchase = purchaseRepository.findById(item.getPurchase().getId())
                .orElseThrow(() -> new Exception("Purchase nuk u gjet me ID: " + item.getPurchase().getId()));
        item.setPurchase(purchase);

        Item dbItem = itemRepository.findById(item.getItem().getId())
                .orElseThrow(() -> new Exception("Artikulli nuk u gjet me ID: " + item.getItem().getId()));

        if (dbItem.getSasia() < item.getQuantity()) {
            throw new Exception("Stok i pamjaftueshem per artikullin: " + dbItem.getEmri());
        }
        dbItem.setSasia(dbItem.getSasia() - item.getQuantity());
        itemRepository.save(dbItem);

        total += dbItem.getCmimi().doubleValue() * item.getQuantity();
        item.setInvoice(invoice);
    }
    invoice.setStatus(InvoiceStatus.PAPAGUAR);
    invoice.setTotali(total);
    return invoiceRepository.save(invoice);
}

public Invoice changeStatus(Long id, InvoiceStatus status) {
    Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found!"));
    if(invoice.getStatus() == InvoiceStatus.PAPAGUAR) {

        invoice.setStatus(InvoiceStatus.PAGUAR);

    }
    return invoiceRepository.save(invoice) ;
}
    @Transactional
    public List<Invoice> getInvoicesByCashier(Long userId) {
      getAuthenticatedUser();
    return invoiceRepository.findByArketarId(userId);
    }
    @Transactional
    public double getDailyTotalByCashier(Long userId) {
     getAuthenticatedUser();
        List<Invoice> invoices = getInvoicesByCashier(userId);
        return invoices.stream().mapToDouble(Invoice::getTotali).sum();
    }


    @Transactional
public List<Invoice> getInvoicesBySector(Long userId) throws Exception {
        User  user=getAuthenticatedUser();

    List<Long> sectorIds = user.getSectors().stream()
            .map(Sector::getId)
            .collect(Collectors.toList());

    return invoiceRepository.findByArketarSectorsIdIn(sectorIds);
}
@Transactional
    public SalesMetrics getSalesMetrics(Long userId) throws Exception {
     getAuthenticatedUser();

    List<Invoice> invoices = getInvoicesByCashier(userId);
    double totalRevenue = invoices.stream().mapToDouble(Invoice::getTotali).sum();
    long totalInvoices = invoices.size();
    long totalItemsSold = invoices.stream()
            .flatMap(invoice -> invoice.getArtikujt().stream())
            .mapToLong(PurchaseItem::getQuantity)
            .sum();

    return new SalesMetrics(totalInvoices, totalItemsSold, totalRevenue);
}

    @Transactional
    public void deleteInvoice(Long invoiceId) throws Exception {
    getAuthenticatedUser();

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new Exception("Fatura nuk u gjet me ID: " + invoiceId));

        for (PurchaseItem item : invoice.getArtikujt()) {
            Item stockItem = itemRepository.findById(item.getItem().getId())
                    .orElseThrow(() -> new Exception("Artikulli nuk u gjet me ID: " + item.getItem().getId()));
            stockItem.setSasia(stockItem.getSasia() + item.getQuantity());
            itemRepository.save(stockItem);
        }

        invoiceRepository.delete(invoice);
    }

    public static class SalesMetrics {
        private final long totalInvoices;
        private final long totalItemsSold;
        private final double totalRevenue;

        public SalesMetrics(long totalInvoices, long totalItemsSold, double totalRevenue) {
            this.totalInvoices = totalInvoices;
            this.totalItemsSold = totalItemsSold;
            this.totalRevenue = totalRevenue;
        }

        public long getTotalInvoices() { return totalInvoices; }
        public long getTotalItemsSold() { return totalItemsSold; }
        public double getTotalRevenue() { return totalRevenue; }
    }
    @Transactional
    public Iterable<Invoice>getAllInvoices() {
    return invoiceRepository.findAll();
    }
    @Transactional
    public Optional<Invoice>getInvoiceById( Long invoiceId) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);
        optionalInvoice.ifPresent(invoice -> {
            System.out.println("Artikujt ne kete invoice:");
            for (PurchaseItem p : invoice.getArtikujt()) {
                System.out.println("Item: " + (p.getItem() != null ? p.getItem().getEmri() : "null")
                        + ", Sasia: " + p.getSasia());
            }
        });
        return optionalInvoice;
    }
}
