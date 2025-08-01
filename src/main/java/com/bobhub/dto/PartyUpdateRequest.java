package com.bobhub.dto;

import com.bobhub.domain.PartyCategory;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyUpdateRequest {
  private Long id;
  private String title;
  private PartyCategory category;
  private Integer limitPeople;
  private Integer limitPrice;
  private LocalDateTime finishedAt;
  private Boolean isOpen;
}
