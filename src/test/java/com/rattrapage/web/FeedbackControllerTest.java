package com.rattrapage.web;

import com.rattrapage.model.*;
import com.rattrapage.repository.FeedbackRepository;
import com.rattrapage.repository.TalkRepository;
import com.rattrapage.repository.UserRepository;
import com.rattrapage.web.FeedbackController.CreateOrUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackControllerTest {

    private FeedbackRepository feedbacks;
    private TalkRepository talks;
    private UserRepository users;
    private FeedbackController controller;

    @BeforeEach
    void setUp() {
        feedbacks = mock(FeedbackRepository.class);
        talks = mock(TalkRepository.class);
        users = mock(UserRepository.class);
        controller = new FeedbackController(feedbacks, talks, users);
    }

    @Test
    void create_shouldReturn409_whenFeedbackAlreadyExists() {

        Long talkId = 1L;
        String email = "attendee@example.com";

        var attendee = new User(email, "hashed", "A Ttendee", Role.ATTENDEE);
        attendee.setId(5L);

        when(users.findByEmail(email)).thenReturn(Optional.of(attendee));
        when(talks.findById(talkId)).thenReturn(Optional.of(new Talk()));
        when(feedbacks.existsByAttendeeIdAndTalkId(5L, talkId)).thenReturn(true);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);

        var dto = new CreateOrUpdate();
        dto.comment = "Super talk !";


        ResponseEntity<?> resp = controller.create(talkId, dto, auth);


        assertEquals(409, resp.getStatusCode().value());
        verify(feedbacks, never()).save(any());
    }

    @Test
    void create_shouldReturn201_whenFirstFeedback() {

        Long talkId = 2L;
        String email = "attendee2@example.com";

        var attendee = new User(email, "hashed", "Another Attendee", Role.ATTENDEE);
        attendee.setId(7L);

        when(users.findByEmail(email)).thenReturn(Optional.of(attendee));
        when(talks.findById(talkId)).thenReturn(Optional.of(new Talk()));
        when(feedbacks.existsByAttendeeIdAndTalkId(7L, talkId)).thenReturn(false);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);

        var dto = new CreateOrUpdate();
        dto.comment = "Très instructif";


        ResponseEntity<?> resp = controller.create(talkId, dto, auth);


        assertEquals(201, resp.getStatusCode().value());
        verify(feedbacks).save(any(Feedback.class));
    }
}
