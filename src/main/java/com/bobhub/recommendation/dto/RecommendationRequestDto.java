package com.bobhub.recommendation.dto;

import com.bobhub.recommendation.domain.Recommendation;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendationRequestDto {
  private String category;
  private String storeName;
  private int star;
  private int totalTime;
  private int pricePerPerson;
  private boolean isReservation;

  public Recommendation toEntity() {
    return Recommendation.builder()
        .category(category)
        .storeName(storeName)
        .star(star)
        .totalTime(totalTime)
        .pricePerPerson(pricePerPerson)
        .isReservation(isReservation)
        .build();
  }
}
