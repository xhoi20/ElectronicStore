package com.electronicstore.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationRequest {
    private Long id;
    private String name;
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
    private String role;
    private Long sectorId;
}
