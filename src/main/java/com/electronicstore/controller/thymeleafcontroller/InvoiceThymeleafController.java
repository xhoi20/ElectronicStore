package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.dto.PurchaseItemRequest;
import com.electronicstore.entity.Invoice;
import com.electronicstore.service.serviceInterface.IInvoiceService;
import com.electronicstore.service.serviceInterface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/invoices")
public class InvoiceThymeleafController {

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private IUserService userService;

    @GetMapping
    public String getAllInvoices(Model model) {
        Iterable<Invoice> invoices = invoiceService.getAllInvoices();
        model.addAttribute("invoices", invoices);
        return "invoice-list";
    }
    @GetMapping("/add")
    public String showCreateInvoiceForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        return "invoice-form";
    }

    @PostMapping("/save")
    public String saveInvoice( @RequestParam Long userId,
                               @RequestParam(value = "itemId", required = false) List<Long> itemIds,
                               @RequestParam(value = "purchaseId", required = false) List<Long> purchaseIds,
                               @RequestParam(value = "quantity", required = false) List<Integer> quantities,
                               RedirectAttributes redirectAttributes)
                               {
        try {

            if (itemIds == null || purchaseIds == null || quantities == null ||
                    itemIds.size() != purchaseIds.size() || itemIds.size() != quantities.size()) {
                throw new IllegalArgumentException("Të dhënat e artikujve janë të pavlefshme ose të paplota.");
            }


            List<PurchaseItemRequest> purchaseItemRequests = new ArrayList<>();
            for (int i = 0; i < itemIds.size(); i++) {
                PurchaseItemRequest request = new PurchaseItemRequest();
                request.setItemId(itemIds.get(i));
                request.setPurchaseId(purchaseIds.get(i));
                request.setQuantity(quantities.get(i));
                purchaseItemRequests.add(request);
            }

            invoiceService.createInvoice(userId,  purchaseItemRequests);
            redirectAttributes.addFlashAttribute("message", "Invoice created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/invoices";
    }

    @GetMapping("/by-cashier/{userId}")
    public String getInvoicesByCashier(@PathVariable Long userId, Model model) {
        model.addAttribute("invoices", invoiceService.getInvoicesByCashier(userId));
        model.addAttribute("cashier", userService.getUserById(userId).orElse(null));
        return "invoices-by-cashier";
    }

    @GetMapping("/by-sector/{userId}")
    public String getInvoicesBySector(@PathVariable Long userId, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("invoices", invoiceService.getInvoicesBySector(userId));
            model.addAttribute("manager", userService.getUserById(userId).orElse(null));
            return "invoices-by-sector";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/invoices";
        }
    }

    @GetMapping("/metrics/{userId}")
    public String getSalesMetrics(@PathVariable Long userId, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("metrics", invoiceService.getSalesMetrics(userId));
            model.addAttribute("manager", userService.getUserById(userId).orElse(null));
            return "sales-metrics";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/invoices";
        }
    }
    @GetMapping("/view/{invoiceId}")
    public String viewInvoiceDetails(@PathVariable Long invoiceId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Invoice> optionalInvoice = invoiceService.getInvoiceById(invoiceId);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            model.addAttribute("invoice", invoice);
            return "invoice-details";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invoice not found");
            return "redirect:/invoices";
        }
    }


    @PostMapping("/delete/{invoiceId}")
    public String deleteInvoice(@PathVariable Long invoiceId,
                                @RequestParam Long userId,
                                RedirectAttributes redirectAttributes) {
        try {
            invoiceService.deleteInvoice( invoiceId);
            redirectAttributes.addFlashAttribute("message", "Invoice deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/invoices";
    }
}
