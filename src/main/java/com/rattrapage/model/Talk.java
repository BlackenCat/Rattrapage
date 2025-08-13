package com.rattrapage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "talks")
public class Talk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank
    @Lob
    @Column(nullable = false)
    private String abstractText;

    @Min(15)
    @Max(90)
    @Column(nullable = false)
    private int durationMinutes;

    @ElementCollection
    @CollectionTable(name = "talk_tags", joinColumns = @JoinColumn(name = "talk_id"))
    @Column(name = "tag", length = 50)
    private List<@NotBlank @Size(max = 50) String> tags = new ArrayList<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id", nullable = false)
    private User speaker;

    public Talk() {}

    public Talk(String title, String abstractText, int durationMinutes, List<String> tags, User speaker) {
        this.title = title;
        this.abstractText = abstractText;
        this.durationMinutes = durationMinutes;
        if (tags != null) this.tags = tags;
        this.speaker = speaker;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public User getSpeaker() { return speaker; }
    public void setSpeaker(User speaker) { this.speaker = speaker; }
}
