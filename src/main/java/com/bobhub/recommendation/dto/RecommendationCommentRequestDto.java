package com.bobhub.recommendation.dto;

import com.bobhub.recommendation.domain.RecommendationComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendationCommentRequestDto {
  private String content;
  private int star;

    public RecommendationComment toEntity(long recommendationId, long userId) {
    return RecommendationComment.builder()
        .recommendationId(recommendationId)
        .userId(userId)
        .star(this.star)
        .content(this.content)
        .build();
  }
}
