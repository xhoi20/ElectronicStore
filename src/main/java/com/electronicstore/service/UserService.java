package com.electronicstore.service;

import com.electronicstore.dto.AuthRequest;
import com.electronicstore.dto.AuthResponse;
import com.electronicstore.dto.UserRegistrationRequest;
import com.electronicstore.dto.UserUpdateRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
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
    public ResponseEntity<Map<String, Object>> registerUser(
            UserRegistrationRequest request, Long sectorId, Set<Long> managedSectorIds) {
        try {

            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return createErrorResponse("Name is missing or empty", HttpStatus.BAD_REQUEST);
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return createErrorResponse("Email is missing or empty", HttpStatus.BAD_REQUEST);
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return createErrorResponse("Password is missing or empty", HttpStatus.BAD_REQUEST);
            }
            if (request.getRole() == null) {
                return createErrorResponse("Role cannot be null", HttpStatus.BAD_REQUEST);
            }

            UserRole role;
            try {
                role = UserRole.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                return createErrorResponse("Invalid role provided", HttpStatus.BAD_REQUEST);
            }

            if (role == null) {
                throw new IllegalArgumentException("Role cannot be null");
            }


           getAuthenticatedUser();

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setRole(role);

            if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
                StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                encryptor.setPassword(encryptionKey);
                encryptor.setAlgorithm(ENCRYPTION_ALGORITHM);
                String encryptedPassword = encryptor.encrypt(request.getPassword());
                user.setPassword(encryptedPassword);
            } else if (user.getId() == null) {
                throw new IllegalArgumentException("Password cannot be set empty for new users");
            }
            user = userRepository.save(user);

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

            return createSuccessResponse(user, "User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return handleException(e);
        }
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
     getAuthenticatedUser();
    if (!userRepository.existsById(id)) {
        throw new RuntimeException("User with ID " + id + " not found.");
    }
    User user = userRepository.findById(id).get();
    List<Purchase> linkedPurchases = purchaseRepository.findByMenaxheriId(id);
    if (!linkedPurchases.isEmpty()) {
        purchaseRepository.deleteAll(linkedPurchases);
    }
    for (Sector linkedSector : new HashSet<>(user.getSectors())) {
        linkedSector.getUsers().remove(user);
        sectorRepository.save(linkedSector);
    }

    userRepository.deleteById(id);
}

    @Transactional
    public ResponseEntity<Map<String,Object>>updateUser(Long id, UserUpdateRequest updateRequest,Long sectorId,Set<Long>managedSectorIds,UserRole requestingUserRole) {
        try{
            if(updateRequest.getName()==null||updateRequest.getName().trim().isEmpty()) {
                return createErrorResponse("Name cannot be empty", HttpStatus.BAD_REQUEST);

            }
            if(updateRequest.getEmail()==null||updateRequest.getEmail().trim().isEmpty()) {
                return createErrorResponse("Email cannot be empty", HttpStatus.BAD_REQUEST);

            }
            if(updateRequest.getRole()==null||updateRequest.getRole().trim().isEmpty()) {
                return createErrorResponse("Role cannot be empty", HttpStatus.BAD_REQUEST);
            }
            UserRole role;
            try{
                role = UserRole.valueOf(updateRequest.getRole().toUpperCase());

            }catch(IllegalArgumentException e){
                return createErrorResponse("Invalid role provided", HttpStatus.BAD_REQUEST);
            }
          getAuthenticatedUser();



            Optional<User> userOptional = userRepository.findById(id);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                user.setName(updateRequest.getName());
                user.setEmail(updateRequest.getEmail());
                user.setRole(role);
                if(role==UserRole.CASHIER && sectorId!=null) {
                    Sector sector=sectorRepository.findById(sectorId).orElseThrow(() -> new IllegalArgumentException("Sector not found"));
                    user.setSector(sector);
                }
                if (role == MANAGER && managedSectorIds != null && !managedSectorIds.isEmpty()) {
                    Iterable<Sector> sectorIterable = sectorRepository.findAllById(managedSectorIds);
                    Set<Sector> sectors = StreamSupport.stream(sectorIterable.spliterator(), false)
                            .collect(Collectors.toSet());
                    if (sectors.size() != managedSectorIds.size()) {
                        throw new IllegalArgumentException("One or more sectors not found");
                    }
                    user.setSectors(sectors);
                }
                return createSuccessResponse(userRepository.save(user), "User updated successfully", HttpStatus.OK);
            }else{
                return createErrorResponse("User not found", HttpStatus.NOT_FOUND);
            }

        }catch(Exception e) {
            return handleException(e);
        }
    }

   @Transactional
    public Iterable<Sector> getAllSectors() {
        return sectorRepository.findAll();
    }
    }


