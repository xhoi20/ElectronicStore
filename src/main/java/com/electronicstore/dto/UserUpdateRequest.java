package com.electronicstore.dto;



import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String email;
    private String role;
}
