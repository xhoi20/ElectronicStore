package com.electronicstore.service;

import com.electronicstore.entity.Sector;
import com.electronicstore.entity.Supplier;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import com.electronicstore.repository.SectorRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.serviceInterface.ISectorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectorService implements ISectorService {
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private UserRepository userRepository;



    private User validateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));
//        System.out.println("User ID: " + user.getId() + ", Role: " + user.getRole() +
//                ", Sector: " + (user.getSector() != null ? user.getSector().getId() : "null"));
        if (user.getRole() != UserRole.MANAGER && user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User must be a manager or admin.");
        }
        return user;
    }

    @Transactional
    public Sector addSector(String sectorName, Long userId) {
        if (sectorName == null || sectorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Sector name cannot be empty");
        }
        User user = validateUser(userId);
        Sector sector = new Sector();
        sector.setSectorName(sectorName);

        sector = sectorRepository.save(sector);
        if (user.getRole().equals("MANAGER")) {
            sector.getUsers().add(user);
        user.getSectors().add(sector);
            userRepository.save(user);
        }
        return sectorRepository.save(sector);
    }
@Transactional
public void deleteSector(Long sectorId) {
   // Sector sector = validateSectorAndUserAccess(sectorId, userId);
    //sector.getUsers().forEach(m -> m.getSectors().remove(sector));

    sectorRepository.deleteById(sectorId);
}

//
//    @Transactional
//    public Sector updateSector(Long sectorId, String newSectorName, Long userId) {
//        if (newSectorName == null || newSectorName.trim().isEmpty()) {
//            throw new IllegalArgumentException("Sector name cannot be empty");
//        }
//        Sector sector = validateSectorAndUserAccess(sectorId, userId);
//        sector.setSectorName(newSectorName);
//        return sectorRepository.save(sector);
//    }
@Transactional
public Sector updateSector(Long sectorId, String newSectorName) {
    Optional<Sector> sectorOptional = sectorRepository.findById(sectorId);
    Sector sector = sectorOptional.get();
    sector.setSectorName(newSectorName);
    return sectorRepository.save(sector);
}
    @Transactional
    public Iterable<Sector> getAllSectors() {
        return sectorRepository.findAll();
    }
        @Transactional
        public Optional<Sector> getSectorById(Long id) {
            return sectorRepository.findById(id);

    }
}
