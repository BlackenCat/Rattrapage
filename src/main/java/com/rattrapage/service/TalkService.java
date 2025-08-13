package com.rattrapage.service;

import com.rattrapage.model.Talk;
import com.rattrapage.model.User;
import com.rattrapage.repository.TalkRepository;
import com.rattrapage.repository.UserRepository;
import com.rattrapage.web.dto.TalkDtos;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TalkService {
    private final TalkRepository talks;
    private final UserRepository users;

    public TalkService(TalkRepository talks, UserRepository users) {
        this.talks = talks;
        this.users = users;
    }

    @Transactional
    public Talk create(TalkDtos.Create dto, Authentication auth) {
        if (talks.existsByTitle(dto.title)) throw new IllegalStateException("Duplicate talk title");
        User speaker = users.findByEmail(auth.getName()).orElseThrow();
        Talk t = new Talk(dto.title, dto.abstractText, dto.durationMinutes, dto.tags, speaker);
        return talks.save(t);
    }

    public List<Talk> list() { return talks.findAll(); }

    public Talk get(Long id) { return talks.findById(id).orElseThrow(); }

    @Transactional
    public Talk update(Long id, TalkDtos.Update dto, Authentication auth) {
        Talk t = talks.findById(id).orElseThrow();
        String current = auth.getName();
        if (!t.getSpeaker().getEmail().equals(current) && auth.getAuthorities().stream().noneMatch(a->a.getAuthority().equals("ROLE_ADMIN")))
            throw new SecurityException("Not owner");
        if (!t.getTitle().equals(dto.title) && talks.existsByTitle(dto.title))
            throw new IllegalStateException("Duplicate talk title");
        t.setTitle(dto.title);
        t.setAbstractText(dto.abstractText);
        t.setDurationMinutes(dto.durationMinutes);
        t.setTags(dto.tags);
        return t;
    }

    @Transactional
    public void delete(Long id, Authentication auth) {
        Talk t = talks.findById(id).orElseThrow();
        String current = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
        if (!t.getSpeaker().getEmail().equals(current) && !isAdmin)
            throw new SecurityException("Not owner");
        talks.delete(t);
    }
}
