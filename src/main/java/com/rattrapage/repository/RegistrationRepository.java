package com.rattrapage.repository;

import com.rattrapage.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByAttendeeIdAndTalkId(Long attendeeId, Long talkId);
    long countByTalkId(Long talkId);
}
