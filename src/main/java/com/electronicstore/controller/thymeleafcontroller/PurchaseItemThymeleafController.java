package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.*;
import com.electronicstore.repository.InvoiceRepository;
import com.electronicstore.repository.ItemRepository;
import com.electronicstore.repository.PurchaseRepository;
import com.electronicstore.service.serviceInterface.IPurchaseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping ("/purchase-items")
public class PurchaseItemThymeleafController {
    @Autowired
    private IPurchaseItemService purchaseItemService;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping
    public String getAllPurchaseItems(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .map(auth -> auth.replace("ROLE_", ""))
                    .orElse("");

            model.addAttribute("email", email);
            model.addAttribute("role", role);
            Iterable<PurchaseItem> purchaseItems = purchaseItemService.getAllPurchaseItems();
            model.addAttribute("purchaseItems", purchaseItems);
        }
        return "purchase-items-list";
    }


    @GetMapping("/add")
    public String showForm(@RequestParam(required = false) Long id,  Model model) {
        PurchaseItem purchaseItem = id != null ? purchaseItemService.getPurchaseItem(id)
                .orElse(new PurchaseItem()) : new PurchaseItem();
        model.addAttribute("purchaseItem", purchaseItem);
        model.addAttribute("purchases", purchaseRepository.findAll());
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("invoices", invoiceRepository.findAll());

        return "purchase-items-form";
    }

    @PostMapping("/save")
    public String savePurchaseItem(@ModelAttribute("purchaseItem") PurchaseItem purchaseItem,

                                   RedirectAttributes redirectAttributes) {
        try {
            Long purchaseId = purchaseItem.getPurchase() != null ? purchaseItem.getPurchase().getId() : null;
            Long itemId = purchaseItem.getItem() != null ? purchaseItem.getItem().getId() : null;
            Integer quantity = purchaseItem.getQuantity();
            Long invoiceId = purchaseItem.getInvoice() != null ? purchaseItem.getInvoice().getId() : null;

            if (purchaseId == null || itemId == null) {
                redirectAttributes.addFlashAttribute("error", "Purchase and Item are required.");
                return "redirect:/purchase-items/form?userId=" ;
            }
            Map<String, Object> purchaseData = new HashMap<>();

            purchaseData.put("purchaseId", purchaseId);
            purchaseData.put("itemId", itemId);
            purchaseData.put("quantity", quantity);
            purchaseData.put("invoiceId", invoiceId);

                purchaseItemService.addPurchaseItem(purchaseData);
                redirectAttributes.addFlashAttribute("message", "Purchase item created.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/purchase-items/form?userId=" ;
        }
        return "redirect:/purchase-items";
    }

    @GetMapping("/edit/{id}")
    public String showUpdatedForm(@PathVariable Long id, Model model) {
        PurchaseItem purchaseItem = id != null ? purchaseItemService.getPurchaseItem(id)
                .orElse(new PurchaseItem()) : new PurchaseItem();
        model.addAttribute("purchaseItem", purchaseItem);
        model.addAttribute("purchases", purchaseRepository.findAll());

        model.addAttribute("invoices", invoiceRepository.findAll());
        return "edit-purchase-items";


    }

    @PostMapping("/edit/{id}")
    public String updatePurchaseItem(@PathVariable Long id, @ModelAttribute("purchaseItem") PurchaseItem purchaseItem) {
        try {
            Long purchaseId = purchaseItem.getPurchase() != null ? purchaseItem.getPurchase().getId() : null;
            Long itemId = purchaseItem.getItem() != null ? purchaseItem.getItem().getId() : null;
            Integer quantity = purchaseItem.getQuantity();
            Long invoiceId = purchaseItem.getInvoice() != null ? purchaseItem.getInvoice().getId() : null;
            Map<String, Object> purchaseData = new HashMap<>();

            purchaseData.put("purchaseId", purchaseId);
            purchaseData.put("itemId", itemId);
            purchaseData.put("quantity", quantity);
            purchaseData.put("invoiceId", invoiceId);
            purchaseItemService.updatePurchaseItem(id,purchaseData);
            return "redirect:/purchase-items";
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return "error";
        }
    }
    @GetMapping("/delete/{id}")
    public String deletePurchaseItem(@PathVariable Long id,  RedirectAttributes redirectAttributes) {
        try {
            purchaseItemService.deletePurchaseItem(id);
            redirectAttributes.addFlashAttribute("message", "Sector deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/purchase-items";
    }


}