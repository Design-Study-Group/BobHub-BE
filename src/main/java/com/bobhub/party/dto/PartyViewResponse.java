package com.bobhub.party.dto;

import com.bobhub.party.domain.PartyCategory;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PartyViewResponse {
  private long id;
  private PartyCategory category;
  private String title;
  private int limitPeople;
  private int limitPrice;
  private Long ownerId;
  private Boolean isOpen;
  private LocalDateTime createdAt;
  private LocalDateTime finishedAt;
  private int currentPeople;
}
