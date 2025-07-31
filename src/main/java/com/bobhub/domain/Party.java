package com.bobhub.domain;

import java.time.LocalDate;
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
  private boolean isOpen;
  private LocalDate createdAt;
  private LocalDate finishedAt;
}
