package com.electronicstore.service;

import com.electronicstore.entity.Sector;
import com.electronicstore.entity.Supplier;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import com.electronicstore.repository.SectorRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.serviceInterface.ISectorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static com.electronicstore.entity.UserRole.MANAGER;

@Service
public class SectorService extends BaseService implements ISectorService {
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private UserRepository userRepository;


 @Transactional
    public ResponseEntity<Map<String, Object>> addSector(Map<String, String> sectorData) {
getAuthenticatedUser();

        try {
            String sectorName = sectorData.get("sectorName");
            if (sectorName == null || sectorName.trim().isEmpty()) {
                return createErrorResponse("Sector name is missing or empty", HttpStatus.BAD_REQUEST);
            }

            Sector sector = new Sector();
            sector.setSectorName(sectorName);
            Sector createdSector = sectorRepository.save(sector);
            return createSuccessResponse(createdSector, "Sector added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return handleException(e);
        }
    }


    @Transactional
    public void deleteSector(Long sectorId) {

         getAuthenticatedUser();
        Optional<Sector> sectorOptional = sectorRepository.findById(sectorId);
        if (sectorOptional.isEmpty()) {
            throw new IllegalArgumentException("Sector not found with id: " + sectorId);
        }

        Sector sector = sectorOptional.get();

        for (User linkedUser : new HashSet<>(sector.getUsers())) {
            linkedUser.getSectors().remove(sector);
            userRepository.save(linkedUser);
        }

        sectorRepository.deleteById(sectorId);
    }
    @Transactional
    public Iterable<Sector> getAllSectors() {
        return sectorRepository.findAll();
    }
        @Transactional
        public Optional<Sector> getSectorById(Long id) {
            return sectorRepository.findById(id);

    }
@Transactional
public ResponseEntity<Map<String, Object>> updateSector( Long sectorId, Map<String, String> sectorData) {
getAuthenticatedUser();

    try {
        String newSectorName = sectorData.get("sectorName");
        if (newSectorName == null || newSectorName.trim().isEmpty()) {
            return createErrorResponse("Sector name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Sector sector = sectorRepository.findById(sectorId)
                .orElseThrow(() -> new EntityNotFoundException("Sector not found with ID: " + sectorId));
        sector.setSectorName(newSectorName);
        Sector updatedSector = sectorRepository.save(sector);
        return createSuccessResponse(updatedSector, "Sector updated successfully", HttpStatus.OK);
    } catch (Exception e) {
        return handleException(e);
    }
}
}
