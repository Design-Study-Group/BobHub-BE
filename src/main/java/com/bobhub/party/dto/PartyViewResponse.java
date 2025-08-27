package com.bobhub.party.dto;

import com.bobhub.party.domain.PartyCategory;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
