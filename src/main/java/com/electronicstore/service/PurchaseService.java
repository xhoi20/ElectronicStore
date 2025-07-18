package com.electronicstore.service;
import com.electronicstore.entity.Purchase;
import com.electronicstore.entity.Supplier;
import com.electronicstore.entity.User;
import com.electronicstore.repository.PurchaseRepository;
import com.electronicstore.repository.SupplierRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.serviceInterface.IPurchaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
@Service
public class PurchaseService extends BaseService implements IPurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private SupplierRepository supplierRepository;
public ResponseEntity<Map<String, Object>> addPurchase( Map<String, Object> purchaseData) {
    User authenticatedUser=getAuthenticatedUser();
    try{

        Long userId = purchaseData.get("userId") != null ?
                Long.valueOf(purchaseData.get("userId").toString()) : null;
        LocalDateTime dataBlerjes = purchaseData.get("dataBlerjes") != null ?
                LocalDateTime.parse(purchaseData.get("dataBlerjes").toString()) : null;
        Long furnitorId = purchaseData.get("furnitorId") != null ?
                Long.valueOf(purchaseData.get("furnitorId").toString()) : null;
        Double totaliKostos = purchaseData.get("totaliKostos") != null ?
                Double.parseDouble(purchaseData.get("totaliKostos").toString()) : null;
        Integer sasia = purchaseData.get("sasia") != null ?
                Integer.parseInt(purchaseData.get("sasia").toString()) : null;
        if (userId == null) {
            return createErrorResponse("ID e perdoruesit nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (dataBlerjes == null) {
            return createErrorResponse("Data e blerjes nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (furnitorId == null) {
            return createErrorResponse("ID e furnitorit nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (totaliKostos == null) {
            return createErrorResponse("Kosto totale nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        if (sasia == null) {
            return createErrorResponse("Sasia nuk mund të jete bosh.", HttpStatus.BAD_REQUEST);
        }
        Supplier furnitor = supplierRepository.findById(furnitorId)
            .orElseThrow(() -> new IllegalArgumentException("Furnitori me ID " + furnitorId + " nuk gjendet."));
        Purchase purchase=Purchase.builder()
            .dataBlerjes(dataBlerjes)
            .furnitori(furnitor)
            .menaxheri(authenticatedUser)
            .totaliKostos(totaliKostos)
            .sasia(sasia)
            .artikujt(new ArrayList<>())
            .build();
        Purchase createPurchase = purchaseRepository.save(purchase);
        return createSuccessResponse(createPurchase, "Blerja u shtua me sukses", HttpStatus.CREATED);
    } catch (Exception e) {
        return handleException(e);
    }
}

    @Transactional
    public void deletePurchase(Long id) {
        getAuthenticatedUser(); // Ensure only managers can delete purchases
        Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
        if (purchaseOptional.isEmpty()) {
            throw new IllegalArgumentException("Blerja me ID "+id + " nuk gjendet.");
        }
        Purchase purchase = purchaseOptional.get();
        purchase.getArtikujt().clear();
        purchaseRepository.save(purchase);
        purchaseRepository.deleteById(id);

        purchaseRepository.deleteById(id);
    }

public ResponseEntity<Map<String, Object>> updatePurchase(Long id, Map<String, Object> purchaseData) {
    getAuthenticatedUser();
    try {
        LocalDateTime dataBlerjes = purchaseData.get("dataBlerjes") != null ?
                LocalDateTime.parse(purchaseData.get("dataBlerjes").toString()) : null;
        Long furnitorId = purchaseData.get("furnitorId") != null ?
                Long.valueOf(purchaseData.get("furnitorId").toString()) : null;
        Double totaliKostos = purchaseData.get("totaliKostos") != null ?
                Double.parseDouble(purchaseData.get("totaliKostos").toString()) : null;
        Integer sasia = purchaseData.get("sasia") != null ?
                Integer.parseInt(purchaseData.get("sasia").toString()) : null;

        Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
        Purchase purchase = purchaseOptional.get();
    if (furnitorId != null) {
        Supplier furnitor = supplierRepository.findById(furnitorId)
                .orElseThrow(() -> new IllegalArgumentException("Furnitori me ID " + furnitorId + " nuk gjendet."));
        purchase.setFurnitori(furnitor);
    }
    if (dataBlerjes != null) {
        purchase.setDataBlerjes(dataBlerjes);
    }
    if (totaliKostos != null) {
        if (totaliKostos < 0) {
            throw new IllegalArgumentException("Kosto totale nuk mund të jetë negative.");
        }
        purchase.setTotaliKostos(totaliKostos);
    }
    if (sasia != null) {
        if (sasia <= 0) {
            throw new IllegalArgumentException("Sasia duhet të jetë më e madhe se zero.");
        }
        purchase.setSasia(sasia);
    }

        Purchase updatedPurchase = purchaseRepository.save(purchase);
        return createSuccessResponse(updatedPurchase, "Blerja u perditesua me sukses", HttpStatus.OK);
    } catch (Exception e) {
        return handleException(e);
    }
}
@Transactional
    public Iterable<Purchase> getAllPurchases() {
    return purchaseRepository.findAll();
}
@Transactional
    public Optional<Purchase> getPurchaseById(Long purchaseId) {
    return purchaseRepository.findById(purchaseId);
}
}
