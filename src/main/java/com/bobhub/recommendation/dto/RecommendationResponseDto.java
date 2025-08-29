package com.bobhub.recommendation.dto;

import com.bobhub.recommendation.domain.Recommendation;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RecommendationResponseDto {
  private long id;
  private long userId;
  private String category;
  private String storeName;
  private float averageRating;
  private int ratingCount;
  private int totalTime;
  private int pricePerPerson;
  private String description;
  private boolean isReservation;
  private List<RecommendationCommentResponseDto> comments;

  public RecommendationResponseDto(Recommendation recommendation) {
    this.id = recommendation.getId();
    this.userId = recommendation.getUserId();
    this.category = recommendation.getCategory();
    this.storeName = recommendation.getStoreName();
    this.averageRating = recommendation.getAverageRating();
    this.ratingCount = recommendation.getRatingCount();
    this.totalTime = recommendation.getTotalTime();
    this.pricePerPerson = recommendation.getPricePerPerson();
    this.description = recommendation.getDescription();
    this.isReservation = recommendation.isReservation();
  }
}
