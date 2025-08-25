package com.bobhub.party.domain;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyMember {
  private long id;
  private long partyId;
  private long userId;
}
