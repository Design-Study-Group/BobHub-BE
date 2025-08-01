package com.bobhub.dto;

import com.bobhub.domain.Party;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyUpdateResponse {
  private Party party;
  private String message;
  private boolean success;
}
