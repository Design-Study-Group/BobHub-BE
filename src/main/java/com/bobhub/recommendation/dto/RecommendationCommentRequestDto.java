package com.bobhub.recommendation.dto;

import com.bobhub.recommendation.domain.RecommendationComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendationCommentRequestDto {
  private String content;

  public RecommendationComment toEntity(long recommendationId, long userId) {
    return RecommendationComment.builder()
        .recommendationId(recommendationId)
        .userId(userId)
        .content(this.content)
        .build();
  }
}
