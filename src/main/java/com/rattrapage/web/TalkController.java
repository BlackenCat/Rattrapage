package com.rattrapage.web;

import com.rattrapage.model.Talk;
import com.rattrapage.web.dto.TalkDtos.*;
import com.rattrapage.service.TalkService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/talks")
public class TalkController {
    private final TalkService service;
    public TalkController(TalkService service){ this.service=service; }

    @PreAuthorize("hasAnyRole('SPEAKER','ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Create dto, Authentication auth) {
        Talk t = service.create(dto, auth);
        return ResponseEntity.status(201).body(
                new Response(t.getId(), t.getTitle(), t.getAbstractText(), t.getDurationMinutes(), t.getTags(),
                        t.getSpeaker().getId(), t.getSpeaker().getEmail())
        );
    }

    @GetMapping
    public List<Response> list() {
        return service.list().stream()
                .map(t -> new Response(t.getId(), t.getTitle(), t.getAbstractText(), t.getDurationMinutes(), t.getTags(),
                        t.getSpeaker().getId(), t.getSpeaker().getEmail()))
                .toList();
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable Long id) {
        Talk t = service.get(id);
        return new Response(t.getId(), t.getTitle(), t.getAbstractText(), t.getDurationMinutes(), t.getTags(),
                t.getSpeaker().getId(), t.getSpeaker().getEmail());
    }

    @PreAuthorize("hasAnyRole('SPEAKER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Update dto, Authentication auth) {
        Talk t = service.update(id, dto, auth);
        return ResponseEntity.ok(new Response(t.getId(), t.getTitle(), t.getAbstractText(), t.getDurationMinutes(), t.getTags(),
                t.getSpeaker().getId(), t.getSpeaker().getEmail()));
    }

    @PreAuthorize("hasAnyRole('SPEAKER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        service.delete(id, auth);
        return ResponseEntity.noContent().build();
    }
}
