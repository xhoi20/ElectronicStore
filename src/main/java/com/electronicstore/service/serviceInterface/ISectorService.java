package com.electronicstore.service.serviceInterface;

import com.electronicstore.entity.Sector;

import java.util.Optional;

public interface ISectorService {
    Sector addSector(String sectorName);
    Sector updateSector(Long sectorId, String newSectorName);
    public void deleteSector(Long sectorId);
Iterable<Sector> getAllSectors();
Optional<Sector> getSectorById(Long id);

}
