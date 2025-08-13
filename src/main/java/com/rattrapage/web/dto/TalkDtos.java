package com.rattrapage.web.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class TalkDtos {
    public static class Create {
        @NotBlank @Size(max=200) public String title;
        @NotBlank public String abstractText;
        @Min(15) @Max(90) public int durationMinutes;
        @NotNull public List<@NotBlank @Size(max=50) String> tags;
    }
    public static class Update {
        @NotBlank @Size(max=200) public String title;
        @NotBlank public String abstractText;
        @Min(15) @Max(90) public int durationMinutes;
        @NotNull public List<@NotBlank @Size(max=50) String> tags;
    }
    public static class Response {
        public Long id;
        public String title;
        public String abstractText;
        public int durationMinutes;
        public List<String> tags;
        public Long speakerId;
        public String speakerEmail;
        public Response(Long id, String title, String abstractText, int durationMinutes,
                        List<String> tags, Long speakerId, String speakerEmail) {
            this.id=id; this.title=title; this.abstractText=abstractText; this.durationMinutes=durationMinutes;
            this.tags=tags; this.speakerId=speakerId; this.speakerEmail=speakerEmail;
        }
    }
}
