package com.rattrapage.repository;

import com.rattrapage.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsByAttendeeIdAndTalkId(Long attendeeId, Long talkId);
}
