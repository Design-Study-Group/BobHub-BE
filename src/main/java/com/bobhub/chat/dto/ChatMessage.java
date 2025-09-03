package com.bobhub.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
  private long partyId;
  private long userId;
  private String sender;

  @JsonProperty("content")
  private String message;
}
