package com.rattrapage.service;

import com.rattrapage.model.Room;
import com.rattrapage.model.Schedule;
import com.rattrapage.model.Talk;
import com.rattrapage.repository.RoomRepository;
import com.rattrapage.repository.ScheduleRepository;
import com.rattrapage.repository.TalkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    private ScheduleRepository schedules;
    private TalkRepository talks;
    private RoomRepository rooms;
    private ScheduleService service;

    @BeforeEach
    void setUp() {
        schedules = mock(ScheduleRepository.class);
        talks = mock(TalkRepository.class);
        rooms = mock(RoomRepository.class);
        service = new ScheduleService(schedules, talks, rooms);
    }

    @Test
    void create_shouldThrow_whenRoomTimeOverlaps() {
        var dto = new ScheduleService.Create();
        dto.talkId = 1L;
        dto.roomId = 10L;
        dto.startTime = LocalDateTime.of(2025, 1, 1, 10, 0);
        dto.endTime   = LocalDateTime.of(2025, 1, 1, 11, 0);

        when(talks.findById(1L)).thenReturn(Optional.of(new Talk()));
        when(rooms.findById(10L)).thenReturn(Optional.of(new Room("Amphi", 100)));
        when(schedules.existsByTalkId(1L)).thenReturn(false);


        when(schedules.existsOverlap(eq(10L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.create(dto));
        verify(schedules, never()).save(any());

    }



    @Test
    void create_shouldPersist_whenNoOverlap() {
        var dto = new ScheduleService.Create();
        dto.talkId = 2L;
        dto.roomId = 20L;
        dto.startTime = LocalDateTime.of(2025, 1, 1, 12, 0);
        dto.endTime   = LocalDateTime.of(2025, 1, 1, 13, 0);

        when(talks.findById(2L)).thenReturn(Optional.of(new Talk()));
        when(rooms.findById(20L)).thenReturn(Optional.of(new Room("B-101", 50)));

        when(schedules.existsByTalkId(2L)).thenReturn(false);
        when(schedules.existsOverlap(eq(20L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);
        when(schedules.save(any(Schedule.class))).thenAnswer(inv -> inv.getArgument(0));

        Schedule saved = service.create(dto);

        verify(talks).findById(2L);
        verify(rooms).findById(20L);

        assertNotNull(saved);
        assertEquals(dto.startTime, saved.getStartTime());
        assertEquals(dto.endTime, saved.getEndTime());
        verify(schedules).save(any(Schedule.class));
    }
}
