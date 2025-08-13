package com.rattrapage.service;

import com.rattrapage.model.*;
import com.rattrapage.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {
    private final RegistrationRepository regs;
    private final UserRepository users;
    private final ScheduleRepository schedules;
    private final TalkRepository talks;

    public RegistrationService(RegistrationRepository regs, UserRepository users, ScheduleRepository schedules, TalkRepository talks) {
        this.regs = regs; this.users=users; this.schedules=schedules; this.talks=talks;
    }

    @Transactional
    public void register(Long talkId, Authentication auth){
        User attendee = users.findByEmail(auth.getName()).orElseThrow();
        if (attendee.getRole() != Role.ATTENDEE) throw new SecurityException("Only ATTENDEE can register");

        Talk talk = talks.findById(talkId).orElseThrow();
        if (!schedules.existsByTalkId(talkId)) throw new IllegalStateException("Talk not scheduled");

        if (regs.existsByAttendeeIdAndTalkId(attendee.getId(), talkId))
            throw new IllegalStateException("Already registered");

        var schedule = schedules.findAll().stream().filter(s -> s.getTalk().getId().equals(talkId)).findFirst().orElseThrow();
        int capacity = schedule.getRoom().getCapacity();
        long count = regs.countByTalkId(talkId);
        if (count >= capacity) throw new IllegalStateException("Room capacity reached");

        regs.save(new Registration(attendee, talk));
    }

    @Transactional
    public void unregister(Long registrationId, Authentication auth){
        Registration r = regs.findById(registrationId).orElseThrow();
        if (!r.getAttendee().getEmail().equals(auth.getName())
                && auth.getAuthorities().stream().noneMatch(a->a.getAuthority().equals("ROLE_ADMIN")))
            throw new SecurityException("Not owner");
        regs.delete(r);
    }
}
