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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

import static com.electronicstore.entity.UserRole.MANAGER;

@Service
public class SectorService extends BaseService implements ISectorService {
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private UserRepository userRepository;




    @Transactional
    public Sector addSector(String sectorName) {
        if (sectorName == null || sectorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Sector name cannot be empty");
        }
        User user = getAuthenticatedUser();


        Sector sector = new Sector();
        sector.setSectorName(sectorName);
        sector = sectorRepository.save(sector);

        if (user.getRole().equals(MANAGER)) {
            sector.getUsers().add(user);
            user.getSectors().add(sector);
            userRepository.save(user);
        }

        return sector;
    }
    @Transactional
    public void deleteSector(Long sectorId) {

        User user = getAuthenticatedUser();
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
public Sector updateSector(Long sectorId, String newSectorName) {
    User user = getAuthenticatedUser();
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
