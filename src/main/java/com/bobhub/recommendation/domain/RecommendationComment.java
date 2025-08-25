package com.bobhub.recommendation.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationComment {
    private long id;
    private long recommendationId;
    private long userId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;
}
