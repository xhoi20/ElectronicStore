package com.electronicstore.dto;



import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateRequest {
    private String name;
    private String email;
    private String role;
}
