package com.rattrapage.web;

import com.rattrapage.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {
    private final RegistrationService service;
    public RegistrationController(RegistrationService service){ this.service=service; }

    @PreAuthorize("hasRole('ATTENDEE')")
    @PostMapping("/talk/{talkId}")
    public ResponseEntity<?> register(@PathVariable Long talkId, Authentication auth){
        service.register(talkId, auth);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unregister(@PathVariable Long id, Authentication auth){
        service.unregister(id, auth);
        return ResponseEntity.noContent().build();
    }
}
