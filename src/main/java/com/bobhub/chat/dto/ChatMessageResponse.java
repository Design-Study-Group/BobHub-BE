package com.bobhub.chat.dto;

import com.bobhub.chat.domain.Chat;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageResponse {
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

  public ChatMessageResponse(Chat chat) {
    chat.setId(id);
    chat.setRoomId(partyId);
    chat.setUserId(userId);
    chat.setSender(sender);
    chat.setContent(content);
    chat.setTimestamp(timestamp);
  }
}
