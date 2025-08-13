package com.rattrapage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "schedules",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "talk_id") // 1 talk = 1 créneau max
        },
        indexes = {
                @Index(name = "idx_schedule_room_time", columnList = "room_id,start_time,end_time")
        }
)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "talk_id", nullable = false, unique = true)
    private Talk talk;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    public Schedule() {}

    public Schedule(Talk talk, Room room, LocalDateTime startTime, LocalDateTime endTime) {
        this.talk = talk;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @AssertTrue(message = "endTime must be after startTime")
    public boolean isChronologicallyValid() {
        if (startTime == null || endTime == null) return true; // laissé à @NotNull
        return endTime.isAfter(startTime);
    }

    public Long getId() { return id; }
    public Talk getTalk() { return talk; }
    public Room getRoom() { return room; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    public void setId(Long id) { this.id = id; }
    public void setTalk(Talk talk) { this.talk = talk; }
    public void setRoom(Room room) { this.room = room; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
