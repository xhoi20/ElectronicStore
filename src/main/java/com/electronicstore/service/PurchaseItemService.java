package com.electronicstore.service;

import com.electronicstore.entity.*;
import com.electronicstore.repository.*;
import com.electronicstore.service.serviceInterface.IPurchaseItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import java.util.Optional;

@Service
public class PurchaseItemService extends BaseService implements IPurchaseItemService {
    @Autowired
    private PurchaseItemRepository purchaseItemRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;
 @Transactional
    public ResponseEntity<Map<String, Object>> addPurchaseItem(@RequestBody Map<String, Object> purchaseData) {
     getAuthenticatedUser();
     try {


         Long purchaseId = purchaseData.get("purchaseId") != null ?
                 Long.valueOf(purchaseData.get("purchaseId").toString()) : null;
         Long itemId = purchaseData.get("itemId") != null ?
                 Long.valueOf(purchaseData.get("itemId").toString()) : null;
         Long invoiceId = purchaseData.get("invoiceId") != null ?
                 Long.valueOf(purchaseData.get("invoiceId").toString()) : null;

         Integer quantity = purchaseData.get("quantity") != null ?
                 Integer.parseInt(purchaseData.get("quantity").toString()) : null;

         if (purchaseId == null) {
             return createErrorResponse("ID E BLERJEVE SMUND TE JETE BOSH.", HttpStatus.BAD_REQUEST);
         }
         if (itemId == null) {
             return createErrorResponse("ID e e produkteve nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
         }
         if (invoiceId == null) {
             return createErrorResponse("Futura nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
         }
         if (quantity == null) {
             return createErrorResponse("Sasia nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
         }

         Purchase purchase = purchaseRepository.findById(purchaseId)
                 .orElseThrow(() -> new EntityNotFoundException("Purchase not found with ID: " + purchaseId));
         Item item = itemRepository.findById(itemId)
                 .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + itemId));
         Invoice invoice = invoiceRepository.findById(invoiceId)
                 .orElseThrow(() -> new EntityNotFoundException("Invoice not found with ID: " + invoiceId));


         PurchaseItem purchaseItem = new PurchaseItem();
         purchaseItem.setPurchase(purchase);
         purchaseItem.setItem(item);
         purchaseItem.setQuantity(quantity);
         purchaseItem.setInvoice(invoice);
         PurchaseItem savePurchaseItem = purchaseItemRepository.save(purchaseItem);
         return createSuccessResponse(savePurchaseItem, "Blerja u shtua me sukses", HttpStatus.CREATED);
     } catch (Exception e) {
         return handleException(e);
     }
 }

        @Transactional
    public void deletePurchaseItem( Long id) {
        getAuthenticatedUser();

        purchaseItemRepository.deleteById(id);
    }
    public ResponseEntity<Map<String, Object>> updatePurchaseItem( Long purchaseItemId, Map<String, Object> purchaseItemData) {
       getAuthenticatedUser();
       try {
            PurchaseItem purchaseItem = purchaseItemRepository.findById(purchaseItemId)
        .orElseThrow(() -> new EntityNotFoundException("Purchase item not found with ID: " + purchaseItemId));
            Integer quantity = purchaseItemData.get("quantity") != null ? Integer.parseInt(purchaseItemData.get("quantity").toString()) : null;

            Long invoiceId = purchaseItemData.get("invoiceId") != null ?
                    Long.valueOf(purchaseItemData.get("invoiceId").toString()) : null;

            if (quantity != null && quantity > 0) {
            purchaseItem.setQuantity(quantity);
        } else if (quantity != null) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        if (invoiceId != null) {
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + invoiceId + " not found."));
            purchaseItem.setInvoice(invoice);
        }

            PurchaseItem updatedPurchaseItem = purchaseItemRepository.save(purchaseItem);
            return createSuccessResponse(updatedPurchaseItem, "Artikulli i blerjes u përditësua me sukses", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @Transactional
    public Iterable<PurchaseItem> getAllPurchaseItems() {
    return purchaseItemRepository.findAll();

    }
    @Transactional
    public Optional<PurchaseItem> getPurchaseItem( Long purchaseItemId) {
       return purchaseItemRepository.findById(purchaseItemId);


    }
}
