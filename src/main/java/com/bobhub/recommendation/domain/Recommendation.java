package com.bobhub.recommendation.domain;

import lombok.*;

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
  private int star;
  private int totalTime;
  private int pricePerPerson;
  private boolean isReservation;

  public void setIsReservation(boolean isReservation) {
    this.isReservation = isReservation;
  }
}
