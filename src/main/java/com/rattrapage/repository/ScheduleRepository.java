package com.rattrapage.repository;

import com.rattrapage.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    boolean existsByTalkId(Long talkId);

    @Query("""
           select (count(s) > 0)
           from Schedule s
           where s.room.id = :roomId
             and s.startTime < :endTime
             and s.endTime   > :startTime
           """)
    boolean existsOverlap(Long roomId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("""
           select (count(s) > 0)
           from Schedule s
           where s.room.id = :roomId
             and s.id <> :excludeId
             and s.startTime < :endTime
             and s.endTime   > :startTime
           """)
    boolean existsOverlapExcluding(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId);

}
