package com.bobhub.recommendation.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {
  private long id;
  private long userId;
  private String category;
  private String storeName;
  private float averageRating;
  private int ratingCount;
  private String description;
  private int totalTime;
  private int pricePerPerson;
  private boolean isReservation;
  private boolean deleted;
  private LocalDateTime deletedAt;

  public void setIsReservation(boolean isReservation) {
    this.isReservation = isReservation;
  }
}
