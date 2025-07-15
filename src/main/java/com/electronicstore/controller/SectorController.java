package com.electronicstore.controller;

import com.electronicstore.entity.Sector;
import com.electronicstore.service.serviceInterface.ISectorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/sectors")
public class SectorController extends BaseController{
    @Autowired
    private ISectorService sectorService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> addSector(@RequestBody Map<String, String> sectorData) {


        try {
            String sectorName = sectorData.get("SectorName");
            if (sectorName == null || sectorName.trim().isEmpty()) {
                return createErrorResponse("Sector name is missing or empty", HttpStatus.BAD_REQUEST);
            }

            checkManagerRole();

            Sector sector = sectorService.addSector(sectorName);
            return createSuccessResponse(sector, "Sector added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{sectorId}")
    public ResponseEntity<Map<String, Object>> deleteSector(@PathVariable Long sectorId) {


        try {
            checkManagerRole();
            sectorService.deleteSector(sectorId);
            return createSuccessResponse(null, "Sector deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PutMapping("/{sectorId}")
    public ResponseEntity<Map<String, Object>> updateSector(@PathVariable Long sectorId, @RequestBody Map<String, String> sectorData) {


        try {
            String newSectorName = sectorData.get("SectorName");
            if (newSectorName == null || newSectorName.trim().isEmpty()) {
                return createErrorResponse("Sector name cannot be empty", HttpStatus.BAD_REQUEST);
            }
            checkManagerRole();
            Sector updatedSector = sectorService.updateSector(sectorId, newSectorName);
            return createSuccessResponse(updatedSector, "Sector updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

}
