package com.rattrapage.web;

import com.rattrapage.model.Room;
import com.rattrapage.repository.RoomRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomRepository rooms;
    public RoomController(RoomRepository rooms){ this.rooms=rooms; }

    public static class Upsert {
        @NotBlank public String name;
        @Min(1) public int capacity;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Upsert dto){
        if (rooms.existsByName(dto.name)) return ResponseEntity.status(409).body("Room name already exists");
        Room r = rooms.save(new Room(dto.name, dto.capacity));
        return ResponseEntity.status(201).body(r);
    }

    @GetMapping public List<Room> list(){ return rooms.findAll(); }

    @GetMapping("/{id}") public Room get(@PathVariable Long id){ return rooms.findById(id).orElseThrow(); }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @Valid @RequestBody Upsert dto){
        Room r = rooms.findById(id).orElseThrow();
        if (!r.getName().equals(dto.name) && rooms.existsByName(dto.name))
            throw new IllegalStateException("Room name already exists");
        r.setName(dto.name); r.setCapacity(dto.capacity);
        return rooms.save(r);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        rooms.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
