package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.Sector;

import com.electronicstore.service.serviceInterface.ISectorService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/sectors")
public class SectorThymeleafController {

    @Autowired
    private ISectorService sectorService;

    @GetMapping
    public String addAllSectors(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .map(auth -> auth.replace("ROLE_", ""))
                    .orElse("");

            model.addAttribute("email", email);
            model.addAttribute("role", role);
        Iterable<Sector>sectors=sectorService.getAllSectors();
        model.addAttribute("sectors", sectors);
        }
        return "sector-list";
    }
    @GetMapping("/add")
    public String AddSector(Model model) {
        model.addAttribute("sector", new Sector());
        return "sector-form";
    }
    @PostMapping("/save")
    public String createSector(@RequestParam Map<String, String> sectorData, RedirectAttributes redirectAttributes) {
        try {

            sectorService.addSector(sectorData);
            redirectAttributes.addFlashAttribute("message", "Sector created successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sectors";
    }
    @GetMapping("/edit/{id}")
    public String showEditSector(@PathVariable Long id, Model model) {
        Sector sector = sectorService.getSectorById(id)
                .orElseThrow(() -> new RuntimeException("Sektori nuk u gjet nuk u gjet"));
        model.addAttribute("sector", sector);
        return "edit-sector";
    }

    @PostMapping("/edit/{id}")
    public String updateSector(@PathVariable Long id, @RequestParam   Map<String, String> sectorData, RedirectAttributes redirectAttributes) {
        try {
            sectorService.updateSector(id, sectorData);
            redirectAttributes.addFlashAttribute("message", "Sector updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sectors";
    }
    @GetMapping("/delete/{id}")
    public String deleteSector(@PathVariable Long id,  RedirectAttributes redirectAttributes) {
        try {
            sectorService.deleteSector(id);
            redirectAttributes.addFlashAttribute("message", "Sector deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sectors";
    }
}
