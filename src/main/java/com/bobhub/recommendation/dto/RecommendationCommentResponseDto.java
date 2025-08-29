package com.bobhub.recommendation.dto;

import com.bobhub.recommendation.domain.RecommendationComment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class RecommendationCommentResponseDto {
    private long id;
    private long recommendationId;
    private long userId;
    private String userName; // 작성자 이름 필드 추가
    private final int star;
    private String content;
    private LocalDateTime createdAt;

  public RecommendationCommentResponseDto(RecommendationComment entity) {
    this.id = entity.getId();
    this.recommendationId = entity.getRecommendationId();
    this.userId = entity.getUserId();
    this.userName = entity.getUserName(); // 생성자에서 초기화
    this.star = entity.getStar();
    this.content = entity.getContent();
    this.createdAt = entity.getCreatedAt();
  }
}
