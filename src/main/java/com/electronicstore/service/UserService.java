package com.electronicstore.service;

import com.electronicstore.dto.AuthRequest;
import com.electronicstore.dto.AuthResponse;
import com.electronicstore.entity.Purchase;
import com.electronicstore.entity.Sector;
import com.electronicstore.entity.User;
import com.electronicstore.entity.UserRole;
import com.electronicstore.repository.PurchaseRepository;
import com.electronicstore.repository.SectorRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.serviceInterface.IUserService;
import com.electronicstore.tokenlogin.JwtUtil;
import jakarta.transaction.Transactional;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.electronicstore.entity.UserRole.MANAGER;

@Service
public class UserService extends BaseService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Value("${jasypt.encryptor.password}")
    private String encryptionKey;

    private static final String ENCRYPTION_ALGORITHM = "PBEWithMD5AndDES";
 @Autowired
    private   JwtUtil jwtUtil;

@Transactional
public User registerUser(String emri, String email, String rawPassword,UserRole role,Long sectorId, Set<Long> managedSectorIds) {

    if (role == null) {
        throw new IllegalArgumentException("Role cannot be null");
    }

    User user = new User();
    user.setName(emri);
    user.setEmail(email);
    user.setRole(role);

    if (rawPassword != null && !rawPassword.trim().isEmpty()) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encryptionKey);
        encryptor.setAlgorithm(ENCRYPTION_ALGORITHM);
        String encryptedPassword = encryptor.encrypt(rawPassword);
        user.setPassword(encryptedPassword);
    } else if (user.getId() ==null) {
        throw new IllegalArgumentException("Password cannot be set empty for new users");
    }
    user = userRepository.save(user);
    User authenticatedUser = getAuthenticatedUser();
    if (authenticatedUser.getRole().equals(MANAGER)) {
        if (role == UserRole.CASHIER && sectorId != null) {
            Sector sector = sectorRepository.findById(sectorId)
                    .orElseThrow(() -> new IllegalArgumentException("Sector not found: " + sectorId));
            user.setSector(sector);
            userRepository.save(user);
        }

        if (role == MANAGER && managedSectorIds != null && !managedSectorIds.isEmpty()) {
            Iterable<Sector> sectorIterable = sectorRepository.findAllById(managedSectorIds);

            Set<Sector> sectors = StreamSupport.stream(sectorIterable.spliterator(), false)
                    .collect(Collectors.toSet());
            if (sectors.size() != managedSectorIds.size()) {
                throw new IllegalArgumentException("One or more sectors not found");
            }

            for (Sector sector : sectors) {

                if (!user.getSectors().contains(sector)) {
                    user.getSectors().add(sector);
                }


            }
            user = userRepository.save(user);
        }
    }
    return user;
}


@Transactional
public AuthResponse loginUser(AuthRequest authRequest) {
    User user = userRepository.findByEmail(authRequest.getEmail());
    if (user == null) {
        throw new RuntimeException("User not found");
    }
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setPassword(encryptionKey);
    encryptor.setAlgorithm(ENCRYPTION_ALGORITHM);
    String decryptedPassword = encryptor.decrypt(user.getPassword());
    if (!decryptedPassword.equals(authRequest.getPassword())) {
        throw new RuntimeException("Invalid credentials");
    }

    if (user.getRole() != UserRole.MANAGER && user.getRole() != UserRole.ADMIN&& user.getRole() != UserRole.CASHIER) {
        throw new IllegalArgumentException("User must be a manager or admin to log in");
    }

    String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    AuthResponse response = new AuthResponse();
    response.setToken(token);
    response.setEmail(user.getEmail());
    response.setRole(user.getRole().name());
    return response;
}
    @Transactional
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    @Transactional
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

@Transactional
public void deleteUserById(Long id) {
    User authenticatedUser = getAuthenticatedUser();
    if (!userRepository.existsById(id)) {
        throw new RuntimeException("User with ID " + id + " not found.");
    }
    User user = userRepository.findById(id).get();
    List<Purchase> linkedPurchases = purchaseRepository.findByMenaxheriId(id);
    if (!linkedPurchases.isEmpty()) {
        purchaseRepository.deleteAll(linkedPurchases); // Fshi të gjitha blerjet e lidhura
    }
    for (Sector linkedSector : new HashSet<>(user.getSectors())) {
        linkedSector.getUsers().remove(user);
        sectorRepository.save(linkedSector);
    }

    userRepository.deleteById(id);
}

    @Transactional
    public User updateUser(Long id, String name, String email,
                           UserRole role, Long sectorId, Set<Long> managedSectorIds) {
        User authenticatedUser = getAuthenticatedUser();
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(name);
            user.setEmail(email);

            user.setRole(role);



            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
    }
   @Transactional
    public Iterable<Sector> getAllSectors() {
        return sectorRepository.findAll();
    }
    }


