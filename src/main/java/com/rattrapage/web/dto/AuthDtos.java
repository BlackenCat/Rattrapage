package com.rattrapage.web.dto;

import com.rattrapage.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

    public static class RegisterRequest {
        @Email @NotBlank public String email;
        @NotBlank public String password;
        @NotBlank public String fullName;
        public Role role;
    }

    public static class LoginRequest {
        @Email @NotBlank public String email;
        @NotBlank public String password;
    }

    public static class AuthResponse {
        public String token;
        public String tokenType = "Bearer";
        public String role;
        public AuthResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }
    }
}
