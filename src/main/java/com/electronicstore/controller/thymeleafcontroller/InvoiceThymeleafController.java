package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.dto.PurchaseItemRequest;
import com.electronicstore.entity.Invoice;
import com.electronicstore.entity.InvoiceStatus;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import com.electronicstore.repository.ItemRepository;
import com.electronicstore.repository.PurchaseRepository;
import com.electronicstore.service.serviceInterface.IInvoiceService;
import com.electronicstore.service.serviceInterface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private ItemRepository itemRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private IUserService userService;

    @GetMapping
    public String getAllInvoices(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.getUserByUsername(email);
            model.addAttribute("email", email);
            model.addAttribute("currentUserId", user != null ? user.getId() : null);


            boolean isManager = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"));

            if (isManager) {
                List<User> cashiers = userService.getUsersByRole(UserRole.CASHIER);
                model.addAttribute("cashiers", cashiers != null ? cashiers : new ArrayList<>());
            } else {
                model.addAttribute("cashiers", new ArrayList<>());
            }

            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .map(auth -> auth.replace("ROLE_", ""))
                    .orElse("");
            model.addAttribute("role", role);
        } else {
            model.addAttribute("cashiers", new ArrayList<>());
        }

        Iterable<Invoice> invoices = invoiceService.getAllInvoices();
        model.addAttribute("invoices", invoices);
        return "invoice-list";
    }


    @GetMapping("/add")
    public String showCreateInvoiceForm(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User user = userService.getUserByUsername(auth.getName());
            model.addAttribute("currentUserId", user != null ? user.getId() : null);
        }
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("purchases", purchaseRepository.findAll());
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



            List<PurchaseItemRequest> purchaseItemRequests = new ArrayList<>();
            for (int i = 0; i < itemIds.size(); i++) {
                PurchaseItemRequest request = new PurchaseItemRequest();
                request.setItemId(itemIds.get(i));
                request.setPurchaseId(purchaseIds.get(i));

                int quantity = (i < quantities.size()) ? quantities.get(i) : 1;
                request.setQuantity(quantity);

                purchaseItemRequests.add(request);

            }

            invoiceService.createInvoice(userId,  purchaseItemRequests);
            redirectAttributes.addFlashAttribute("message", "Invoice created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/invoices";
    }

    @GetMapping("/by-cashier")
    @PreAuthorize("hasRole('MANAGER')")
    public String getInvoicesByCashier(@RequestParam Long userId, Model model) {
        model.addAttribute("invoices", invoiceService.getInvoicesByCashier(userId));
        model.addAttribute("cashier", userService.getUserById(userId).orElse(null));
        return "invoices-by-cashier";
    }

//    @GetMapping("/by-sector/{userId}")
//    public String getInvoicesBySector(@PathVariable Long userId, Model model, RedirectAttributes redirectAttributes) {
//        try {
//            model.addAttribute("invoices", invoiceService.getInvoicesBySector(userId));
//            model.addAttribute("manager", userService.getUserById(userId).orElse(null));
//            return "invoices-by-sector";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//            return "redirect:/invoices";
//        }
//    }

    @GetMapping("/metrics")
    @PreAuthorize("hasRole('MANAGER')")
    public String getSalesMetrics(@RequestParam Long userId, Model model, RedirectAttributes redirectAttributes) {
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
    @PutMapping("/status/{invoiceId}")
    public String changeStatusInvoice(@PathVariable Long id,@RequestParam InvoiceStatus status,
                                RedirectAttributes redirectAttributes) {
        try {
            invoiceService.changeStatus(id, status);
            redirectAttributes.addFlashAttribute("message", "Invoice deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/invoices";
    }

    @PostMapping("/delete/{invoiceId}")
    public String deleteInvoice(@PathVariable Long invoiceId,
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
