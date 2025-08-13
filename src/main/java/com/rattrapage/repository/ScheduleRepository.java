package com.rattrapage.repository;

import com.rattrapage.model.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByRoomIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long roomId,
            LocalDateTime newEnd,
            LocalDateTime newStart
    );

    boolean existsByTalkId(Long talkId);
}
