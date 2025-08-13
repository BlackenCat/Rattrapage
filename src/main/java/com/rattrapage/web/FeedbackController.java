package com.rattrapage.web;

import com.rattrapage.model.Feedback;
import com.rattrapage.model.Role;
import com.rattrapage.model.Talk;
import com.rattrapage.model.User;
import com.rattrapage.repository.FeedbackRepository;
import com.rattrapage.repository.TalkRepository;
import com.rattrapage.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@Tag(name="Feedback", description="Mise en place des feedback")
@RestController
public class FeedbackController {

    private final FeedbackRepository feedbacks;
    private final TalkRepository talks;
    private final UserRepository users;

    public FeedbackController(FeedbackRepository feedbacks, TalkRepository talks, UserRepository users) {
        this.feedbacks = feedbacks;
        this.talks = talks;
        this.users = users;
    }

    public static class CreateOrUpdate {
        @Size(max = 5000)
        public String comment; // optionnel
    }

    public static class FeedbackResponse {
        public Long id;
        public Long talkId;
        public Long attendeeId;
        public String attendeeEmail;
        public String comment;

        public FeedbackResponse(Feedback f) {
            this.id = f.getId();
            this.talkId = f.getTalk().getId();
            this.attendeeId = f.getAttendee().getId();
            this.attendeeEmail = f.getAttendee().getEmail();
            this.comment = f.getComment();
        }
    }

    @PreAuthorize("hasRole('ATTENDEE')")
    @PostMapping("/talks/{talkId}/feedback")
    @Transactional
    public ResponseEntity<?> create(@PathVariable Long talkId,
                                    @Valid @RequestBody CreateOrUpdate dto,
                                    Authentication auth) {

        User attendee = users.findByEmail(auth.getName()).orElseThrow();
        if (attendee.getRole() != Role.ATTENDEE) {
            return ResponseEntity.status(403).body("Only ATTENDEE can leave feedback");
        }

        Talk talk = talks.findById(talkId).orElseThrow();

        if (feedbacks.existsByAttendeeIdAndTalkId(attendee.getId(), talkId)) {
            return ResponseEntity.status(409).body("Feedback already exists for this talk by this attendee");
        }

        Feedback f = new Feedback(attendee, talk, dto.comment);
        feedbacks.save(f);
        return ResponseEntity.status(201).body(new FeedbackResponse(f));
    }

    @GetMapping("/talks/{talkId}/feedback")
    public List<FeedbackResponse> listForTalk(@PathVariable Long talkId) {
        return feedbacks.findByTalkId(talkId).stream()
                .map(FeedbackResponse::new)
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/feedback/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody CreateOrUpdate dto,
                                    Authentication auth) {
        Feedback f = feedbacks.findById(id).orElseThrow();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!f.getAttendee().getEmail().equals(auth.getName()) && !isAdmin) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        f.setComment(dto.comment);
        return ResponseEntity.ok(new FeedbackResponse(f));
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/feedback/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        Feedback f = feedbacks.findById(id).orElseThrow();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!f.getAttendee().getEmail().equals(auth.getName()) && !isAdmin) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        feedbacks.delete(f);
        return ResponseEntity.noContent().build();
    }
}
