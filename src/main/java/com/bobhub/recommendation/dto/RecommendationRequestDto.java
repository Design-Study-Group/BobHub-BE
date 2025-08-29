package com.bobhub.recommendation.dto;

import com.bobhub.recommendation.domain.Recommendation;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendationRequestDto {
  private String category;
  private String storeName;
  private int totalTime;
  private int pricePerPerson;
  private String description;
  private boolean isReservation;

  public Recommendation toEntity() {
    return Recommendation.builder()
        .category(category)
        .storeName(storeName)
        .averageRating(0.0f) // 초기 평점은 0으로 설정
        .ratingCount(0)      // 초기 평가자 수는 0으로 설정
        .totalTime(totalTime)
        .pricePerPerson(pricePerPerson)
        .description(description)
        .isReservation(isReservation)
        .build();
  }
}
