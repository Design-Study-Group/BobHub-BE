package com.bobhub.recommendation.domain;

import java.time.LocalDateTime;
import lombok.*;

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
  private int star;
  private String content;
  private LocalDateTime createdAt;
}
