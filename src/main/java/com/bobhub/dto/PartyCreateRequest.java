package com.bobhub.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PartyCreateRequest {
  private String title;
  private String limitPeople;
  private Integer limitPrice;
  private String finishedAt;
  private String category;
  private Long ownerId;
  private Boolean isOpen;
}
