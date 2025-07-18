package com.electronicstore.controller;
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
            checkManagerRole();
            sectorService.addSector(sectorData);
            return createSuccessResponse( sectorService.addSector(sectorData), "Sector added successfully", HttpStatus.CREATED);
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
            checkManagerRole();
            sectorService.updateSector(sectorId, sectorData);
            return createSuccessResponse(sectorService.updateSector(sectorId, sectorData), "Sector updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

}
