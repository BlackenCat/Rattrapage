package com.rattrapage.web;

import com.rattrapage.model.Schedule;
import com.rattrapage.repository.ScheduleRepository;
import com.rattrapage.service.ScheduleService;
import com.rattrapage.service.ScheduleService.Create;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService service;
    private final ScheduleRepository repo;

    public ScheduleController(ScheduleService service, ScheduleRepository repo) {
        this.service = service; this.repo = repo;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Create dto){
        Schedule s = service.create(dto);
        return ResponseEntity.status(201).body(s);
    }

    @GetMapping public List<Schedule> list(){ return repo.findAll(); }

    @GetMapping("/{id}") public Schedule get(@PathVariable Long id){ return repo.findById(id).orElseThrow(); }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<Schedule> update(
            @PathVariable Long id,
            @jakarta.validation.Valid @RequestBody Create dto
    ) {
        Schedule s = service.update(id, dto);
        return org.springframework.http.ResponseEntity.ok(s);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return org.springframework.http.ResponseEntity.noContent().build();
    }
}
