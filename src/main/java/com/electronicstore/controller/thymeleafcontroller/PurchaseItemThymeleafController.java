package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.*;
import com.electronicstore.repository.InvoiceRepository;
import com.electronicstore.repository.ItemRepository;
import com.electronicstore.repository.PurchaseRepository;
import com.electronicstore.service.ItemService;
import com.electronicstore.service.PurchaseItemService;
import com.electronicstore.service.serviceInterface.IPurchaseItemService;
import com.electronicstore.service.serviceInterface.IPurchaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;

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
    public String getAllPurchaseItems(Model model) {
        Iterable<PurchaseItem> purchaseItems = purchaseItemService.getAllPurchaseItems();
        model.addAttribute("purchaseItems", purchaseItems);
        return "purchase-items-list";
    }


    @GetMapping("/add")
    public String showForm(@RequestParam(required = false) Long id, @RequestParam Long userId, Model model) {
        PurchaseItem purchaseItem = id != null ? purchaseItemService.getPurchaseItem(id)
                .orElse(new PurchaseItem()) : new PurchaseItem();
        model.addAttribute("purchaseItem", purchaseItem);
        model.addAttribute("purchases", purchaseRepository.findAll());
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("invoices", invoiceRepository.findAll());
        model.addAttribute("userId", userId);
        return "purchase-items-form";
    }

    @PostMapping("/save")
    public String savePurchaseItem(@ModelAttribute("purchaseItem") PurchaseItem purchaseItem,
                                   @RequestParam Long userId,
                                   RedirectAttributes redirectAttributes) {
        try {
            Long purchaseId = purchaseItem.getPurchase() != null ? purchaseItem.getPurchase().getId() : null;
            Long itemId = purchaseItem.getItem() != null ? purchaseItem.getItem().getId() : null;
            Integer quantity = purchaseItem.getQuantity();
            Long invoiceId = purchaseItem.getInvoice() != null ? purchaseItem.getInvoice().getId() : null;

            if (purchaseId == null || itemId == null) {
                redirectAttributes.addFlashAttribute("error", "Purchase and Item are required.");
                return "redirect:/purchase-items/form?userId=" + userId;
            }

            if (purchaseItem.getId() == null) {
                purchaseItemService.addPurchaseItem(userId, purchaseId, itemId, quantity, invoiceId);
                redirectAttributes.addFlashAttribute("message", "Purchase item created.");
            } else {
                purchaseItemService.updatePurchaseItem(purchaseItem.getId(), itemId, quantity, invoiceId);
                redirectAttributes.addFlashAttribute("message", "Purchase item updated.");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/purchase-items/form?userId=" + userId;
        }
        return "redirect:/purchase-items";
    }

    @GetMapping("/edit/{id}")
    public String showForm(@PathVariable Long id, Model model) {
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
            purchaseItemService.updatePurchaseItem(id, purchaseItem.getPurchase().getId(), purchaseItem.getQuantity(),purchaseItem.getInvoice().getId());
            return "redirect:/purchase-items";
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return "error";
        }
    }
    @PostMapping("/delete/{id}")
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