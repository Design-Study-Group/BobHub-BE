package com.bobhub.party.domain;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Party {
  private long id;
  private PartyCategory category;
  private String title;
  private int limitPeople;
  private int limitPrice;
  private long ownerId;
  private long memberId;
  private Boolean isOpen;
  private LocalDateTime createdAt;
  private LocalDateTime finishedAt;
  private int currentPeople;
}
