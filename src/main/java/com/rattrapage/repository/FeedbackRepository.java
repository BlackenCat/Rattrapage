package com.rattrapage.repository;

import com.rattrapage.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByAttendeeIdAndTalkId(Long attendeeId, Long talkId);

    List<Feedback> findByTalkId(Long talkId);
}
