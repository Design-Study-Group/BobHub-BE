package com.bobhub.domain;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comments {
  private long id;
  private long partyId;
  private long writerId;
  private String name;
  private String comments;
  Date createdAt;
}
