package com.bobhub.chat;

import com.bobhub.chat.dto.ChatMessage;
import com.bobhub.chat.dto.ChatRoom;
import com.bobhub.chat.service.ChatService;
import com.bobhub.party.mapper.PartyMapper;
import com.bobhub.user.domain.User;
import com.bobhub.user.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSockChatHandler extends TextWebSocketHandler {

  private final ObjectMapper objectMapper;
  private final UserMapper userMapper;
  private final PartyMapper partyMapper;
  private final ChatService chatService;

  // 세션별 방 관리
  private final Map<Long, ChatRoom> partyIdMap = new ConcurrentHashMap<>();
  private final Map<WebSocketSession, ChatRoom> sessionRoomMap = new ConcurrentHashMap<>();

  public Long findUserByEmail(WebSocketSession session) {
    Principal principal = session.getPrincipal();
    assert principal != null;
    String email = principal.getName();

    User user = userMapper.findByEmail(email);

    return user.getId();
  }

  public void joinChatRoom(WebSocketSession session, Long partyId, ChatRoom room) {
    Long userId = findUserByEmail(session);
    if (partyMapper.isJoinParty(partyId, userId) || partyMapper.isPartyOwner(partyId, userId)) {
      room.addSession(session);
      sessionRoomMap.put(session, room);
    }
  }

  public void createChatRoom(WebSocketSession session, ChatRoom room, Long partyId)
      throws IOException {
    LocalDateTime now = LocalDateTime.now();

    LocalDateTime targetTime = partyMapper.getPartyById(partyId).getFinishedAt();

    System.out.println("now=" + now);
    System.out.println("targetTime=" + targetTime);
    System.out.println("now.isBefore(targetTime)" + now.isBefore(targetTime));
    if (now.isBefore(targetTime)) {
      partyIdMap.put(partyId, room);
      return;
    }

    System.out.println("방이 생성되지 않음");
    session.sendMessage(new TextMessage("{\"type\":\"CLOSED\",\"message\":\"해당 파티는 종료되었습니다.\"}"));
  }

  // 연결 시 URL에서 partyId를 파싱하고 방에 등록
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    try {

      String path = session.getUri().getPath(); // 예: /ws/chat/84
      String[] segments = path.split("/");
      Long partyId = Long.parseLong(segments[segments.length - 1]);

      ChatRoom room = chatService.findRoomById(partyId);
      if (room == null) {
        System.out.println("유효하지 않은 방 ID: " + partyId);
        session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid partyId"));
        return;
      }
      // 파티 채팅방 생성
      createChatRoom(session, room, partyId);

      // 파티에 사용자 등록
      joinChatRoom(session, partyId, room);

      log.warn(
          "세션 추가됨: {}, 방: {}, 참가자수 : {} ", session.getId(), partyId, room.getSessions().size());
      log.warn("세션 연결됨: sessionId={}, partyID={}", session.getId(), partyId);

    } catch (Exception e) {
      log.error("연결 처리 실패", e);
      session.close(CloseStatus.SERVER_ERROR.withReason("Connection error"));
    }
  }

  // 메시지 처리
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    // 세션 기반으로 방 조회
    ChatRoom room = sessionRoomMap.get(session);
    if (room == null) {
      log.warn("세션이 속한 방을 찾을 수 없음: sessionId={}", session.getId());
      session.sendMessage(new TextMessage("{\"type\":\"CLOSED\",\"message\":\"파티에 참가되지 않았습니다.\"}"));
      return;
    }
    if (!session.isOpen()) {
      room.removeSession(session);
      sessionRoomMap.remove(session);
    }
    String payload = message.getPayload();
    System.out.println("수신 메시지: " + payload);

    ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

    // 메시지 처리
    room.handleActions(session, chatMessage, chatService);
  }

  // 연결 종료 시 세션 제거
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    ChatRoom room = sessionRoomMap.remove(session.getId());
    if (room != null) {
      room.removeSession(session); // ChatRoom에서 세션 제거 메서드
    }
    log.info("세션 종료: sessionId={}, status={}", session.getId(), status);
  }
}
