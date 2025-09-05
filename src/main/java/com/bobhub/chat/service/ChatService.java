package com.bobhub.chat.service;

import com.bobhub.chat.domain.ChatRoom;
import com.bobhub.chat.dto.ChatMessageResponse;
import com.bobhub.chat.mapper.ChatMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

  private final ObjectMapper objectMapper;
  private final ChatMapper chatMapper;
  private Map<Long, ChatRoom> chatRooms;

  @PostConstruct
  private void init() {
    chatRooms = new LinkedHashMap<>();
  }

  public List<ChatRoom> findAllRoom() {
    return new ArrayList<>(chatRooms.values());
  }

  public ChatRoom findRoomById(long partyId) {
    // 1. 기존 채팅방이 있는지 확인
    log.info("chatRooms of partyId : {}", chatRooms.get(partyId));
    if (chatRooms.get(partyId) != null) {
      long existingRoom = chatRooms.get(partyId).getPartyId();
      if (existingRoom == partyId) {
        return chatRooms.get(partyId);
      }
    }

    log.info("ChatRoom: {}", chatRooms.get(partyId));
    // 2. 없으면 새로 생성
    return createRoom(partyId); // 새로 생성된 채팅방 반환 (ID 포함)
  }

  public ChatRoom createRoom(long partyId) {
    ChatRoom chatRoom = ChatRoom.builder().partyId(partyId).build();
    chatRooms.put(partyId, chatRoom);
    return chatRoom;
  }

  public <T> void sendMessage(WebSocketSession session, T content) {
    try {
      session.sendMessage(new TextMessage(objectMapper.writeValueAsString(content)));

    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public List<ChatMessageResponse> getChatHistory(Long partyId) {
    return chatMapper.getChatsByPartyId(partyId);
  }
}
