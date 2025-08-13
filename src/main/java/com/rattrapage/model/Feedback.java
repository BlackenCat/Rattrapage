package com.rattrapage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
        name = "feedbacks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"attendee_id", "talk_id"})
        }
)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "attendee_id", nullable = false)
    private User attendee;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "talk_id", nullable = false)
    private Talk talk;

    @Lob
    private String comment;

    public Feedback() {}

    public Feedback(User attendee, Talk talk, String comment) {
        this.attendee = attendee;
        this.talk = talk;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public User getAttendee() { return attendee; }
    public Talk getTalk() { return talk; }
    public String getComment() { return comment; }

    public void setId(Long id) { this.id = id; }
    public void setAttendee(User attendee) { this.attendee = attendee; }
    public void setTalk(Talk talk) { this.talk = talk; }
    public void setComment(String comment) { this.comment = comment; }
}
