package com.rattrapage.web;

import com.rattrapage.model.Role;
import com.rattrapage.model.User;
import com.rattrapage.repository.UserRepository;
import com.rattrapage.security.jwt.JwtService;
import com.rattrapage.web.dto.AuthDtos.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final AuthenticationManager authManager;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt, AuthenticationManager authManager) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (users.existsByEmail(req.email)) {
            return ResponseEntity.status(409).body("Email already in use");
        }
        Role role = (req.role == null) ? Role.ATTENDEE : req.role; // par défaut ATTENDEE
        User u = new User(req.email, encoder.encode(req.password), req.fullName, role);
        users.save(u);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        var authToken = new UsernamePasswordAuthenticationToken(req.email, req.password);
        authManager.authenticate(authToken); // lève une exception si invalide
        var user = users.findByEmail(req.email).orElseThrow();
        String token = jwt.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));
    }
}
