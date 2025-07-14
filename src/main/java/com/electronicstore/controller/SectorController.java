package com.electronicstore.controller;

import com.electronicstore.entity.Sector;
import com.electronicstore.service.serviceInterface.ISectorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/sector")
public class SectorController {
    @Autowired
    private ISectorService sectorService;


@PostMapping
public ResponseEntity<Sector> addSector(@RequestParam Long managerId, @RequestBody Map<String, String> sectorData) {
    try {
        String sectorName = sectorData.get("SectorName");
        if (sectorName == null || sectorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Sector name is missing or empty");
        }
        Sector sectori = sectorService.addSector(sectorName, managerId);
        return new ResponseEntity<>(sectori, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        System.out.println("Exception: " + e.getMessage());
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
//    @PutMapping("/{sectorId}")
//    public ResponseEntity<Sector> updateSector(@PathVariable Long sectorId, @RequestParam String newSectorName, @RequestParam Long managerId) {
//        try {
//            Sector updatedSector = sectorService.updateSector(sectorId, newSectorName, managerId);
//            return new ResponseEntity<>(updatedSector, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//    }

//    @DeleteMapping("/{sectorId}")
//    public ResponseEntity<Void> deleteSector(@PathVariable Long sectorId, @RequestParam Long managerId) {
//        try {
//            sectorService.deleteSector(sectorId, managerId);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
}
