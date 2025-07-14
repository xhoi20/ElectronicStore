package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.entity.Sector;

import com.electronicstore.service.serviceInterface.ISectorService;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/sectors")
public class SectorThymeleafController {

    @Autowired
    private ISectorService sectorService;

    @GetMapping
    public String addAllSectors(Model model) {
        Iterable<Sector>sectors=sectorService.getAllSectors();
        model.addAttribute("sectors", sectors);
        return "sector-list";
    }
    @GetMapping("/add")
    public String AddSector(Model model) {
        model.addAttribute("sector", new Sector());
        return "sector-form";
    }
    @PostMapping
    public String createSector(@RequestParam String sectorName, @RequestParam Long userId, RedirectAttributes redirectAttributes) {
        try {
            sectorService.addSector(sectorName, userId);
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
    public String updateSector(@PathVariable Long id, @RequestParam String sectorName, RedirectAttributes redirectAttributes) {
        try {
            sectorService.updateSector(id, sectorName);
            redirectAttributes.addFlashAttribute("message", "Sector updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sectors";
    }
    @PostMapping("/delete/{id}")
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
