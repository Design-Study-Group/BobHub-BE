package com.bobhub.domain;

import lombok.*;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {
  private long id;
  private String storeName;
  private Integer star;
  private Integer totalTime;
  private Integer pricePerPerson;
  private boolean isReservation;

  public void setIsReservation(boolean isReservation) {
    this.isReservation = isReservation;
  }
}
