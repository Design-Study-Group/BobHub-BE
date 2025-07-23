package com.bobhub.domain;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Party {
  private long id;
  private String title;
  private int limitPeople;
  private int limitPrice;
  private long ownerId;
  private long memberId;
  private boolean isOpen;

  private Date createdAt;
  private Date finishedAt;
}
