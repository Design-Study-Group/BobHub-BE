package com.bobhub.chat.domain;

import java.sql.Timestamp;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
  private Long id;
  private Long roomId;
  private String sender;
  private Long userId;
  private String content;
  private Timestamp timestamp;
}
