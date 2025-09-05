package com.bobhub.chat.domain;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
  public enum MessageType {
    ENTER,
    TALK
  }

  private MessageType type = MessageType.TALK;
  private long id;
  private long partyId;
  private long userId;
  private String sender;
  private String content;
  private Timestamp timestamp;
}
