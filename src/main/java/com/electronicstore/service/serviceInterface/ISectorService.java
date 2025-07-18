package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.Sector;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public interface ISectorService {
    public ResponseEntity<Map<String, Object>> addSector(Map<String, String> sectorData);
    public ResponseEntity<Map<String, Object>> updateSector( Long sectorId, Map<String, String> sectorData);
    public void deleteSector(Long sectorId);
Iterable<Sector> getAllSectors();
Optional<Sector> getSectorById(Long id);

}
