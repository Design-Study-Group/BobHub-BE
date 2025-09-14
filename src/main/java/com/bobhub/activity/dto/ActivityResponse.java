package com.bobhub.activity.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityResponse {
    private String type;
    private long referenceId;
    @JsonIgnore
    private String title;
    private LocalDateTime createdAt;
    private String category;
    private long userId;
    @JsonIgnore
    private String userName;

    public String getMessage() {
        if ("party".equals(type)) {
            return String.format("%s님이 '%s' 파티를 만들었어요.", userName, title);
        } else if ("recommendation".equals(type)) {
            return String.format("%s님이 '%s' 맛집을 추천했어요.", userName, title);
        }
        return "새로운 활동이 있습니다.";
    }
}
