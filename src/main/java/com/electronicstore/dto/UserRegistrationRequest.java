package com.electronicstore.dto;
import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String name;
    private String email;
    private String password;
    private String role;
    private Long sectorId;
}
