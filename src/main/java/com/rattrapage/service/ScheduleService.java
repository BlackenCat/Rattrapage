package com.rattrapage.service;

import com.rattrapage.model.Room;
import com.rattrapage.model.Schedule;
import com.rattrapage.model.Talk;
import com.rattrapage.repository.RoomRepository;
import com.rattrapage.repository.ScheduleRepository;
import com.rattrapage.repository.TalkRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ScheduleService {

    public static class Create {
        @NotNull public Long talkId;
        @NotNull public Long roomId;
        @NotNull public LocalDateTime startTime;
        @NotNull public LocalDateTime endTime;
    }

    private final ScheduleRepository schedules;
    private final TalkRepository talks;
    private final RoomRepository rooms;

    public ScheduleService(ScheduleRepository schedules, TalkRepository talks, RoomRepository rooms) {
        this.schedules = schedules;
        this.talks = talks;
        this.rooms = rooms;
    }

    @Transactional
    public Schedule create(@Valid Create dto) {
        if (dto.endTime.isBefore(dto.startTime) || dto.endTime.equals(dto.startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }

        Talk talk = talks.findById(dto.talkId).orElseThrow();
        Room room = rooms.findById(dto.roomId).orElseThrow();

        if (schedules.existsByTalkId(dto.talkId)) {
            throw new IllegalStateException("Talk already scheduled");
        }
        if (schedules.existsOverlap(dto.roomId, dto.startTime, dto.endTime)) {
            throw new IllegalStateException("Room/time slot overlaps");
        }

        Schedule s = new Schedule(talk, room, dto.startTime, dto.endTime);
        return schedules.save(s);
    }

    @Transactional
    public Schedule update(Long id, @Valid Create dto) {
        Schedule current = schedules.findById(id).orElseThrow();

        if (dto.endTime.isBefore(dto.startTime) || dto.endTime.equals(dto.startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }

        Talk talk = talks.findById(dto.talkId).orElseThrow();
        Room room = rooms.findById(dto.roomId).orElseThrow();

        if (!current.getTalk().getId().equals(talk.getId()) && schedules.existsByTalkId(dto.talkId)) {
            throw new IllegalStateException("Talk already scheduled");
        }

        if (schedules.existsOverlapExcluding(dto.roomId, dto.startTime, dto.endTime, id)) {
            throw new IllegalStateException("Room/time slot overlaps");
        }

        current.setTalk(talk);
        current.setRoom(room);
        current.setStartTime(dto.startTime);
        current.setEndTime(dto.endTime);
        return current;
    }

    @Transactional
    public void delete(Long id) {
        if (!schedules.existsById(id)) {
            throw new java.util.NoSuchElementException("Schedule not found");
        }
        schedules.deleteById(id);
    }
}
