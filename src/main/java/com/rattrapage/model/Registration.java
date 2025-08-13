package com.rattrapage.model;

import jakarta.persistence.*;

@Entity
@Table(name="registrations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"attendee_id","talk_id"})
})
public class Registration {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="attendee_id")
    private User attendee;

    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="talk_id")
    private Talk talk;

    public Registration() {}
    public Registration(User attendee, Talk talk){ this.attendee=attendee; this.talk=talk; }

    public Long getId(){ return id; }
    public User getAttendee(){ return attendee; }
    public Talk getTalk(){ return talk; }
}
