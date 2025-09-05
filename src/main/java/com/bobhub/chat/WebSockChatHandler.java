package com.bobhub.chat;

import com.bobhub.chat.domain.Chat;
import com.bobhub.chat.domain.ChatMessage;
import com.bobhub.chat.domain.ChatRoom;
import com.bobhub.chat.mapper.ChatMapper;
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
  private final ChatMapper chatMapper;
  private final ChatService chatService;

  // 세션별 방 관리
  private final Map<Long, ChatRoom> partyIdMap = new ConcurrentHashMap<>();

  // 세션룸에 세션 및 채팅방 저장
  private final Map<WebSocketSession, ChatRoom> sessionRoomMap = new ConcurrentHashMap<>();

  // email로 사용자 구분하기
  public Long findUserByEmail(WebSocketSession session) {
    Principal principal = session.getPrincipal();

    if (principal == null) {
      return null;
    }

    User user = userMapper.findByEmail(principal.getName());
    return user.getId();
  }

  // 채팅룸에 맴버 조인하기
  public void joinChatRoom(WebSocketSession session, Long partyId, ChatRoom room) {
    Long userId = findUserByEmail(session);
    if (partyMapper.isJoinParty(partyId, userId) || partyMapper.isPartyOwner(partyId, userId)) {
      room.addSession(session);
      sessionRoomMap.put(session, room);
    }
  }

  // 채팅 룸 생성하기
  public void createChatRoom(WebSocketSession session, ChatRoom room, Long partyId)
      throws IOException {

    if (room == null) {
      log.error("유효하지 않은 방 ID: {}", partyId);
      session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid partyId"));
      return;
    }

    LocalDateTime now = LocalDateTime.now();

    LocalDateTime targetTime = partyMapper.getPartyById(partyId).getFinishedAt();

    if (now.isBefore(targetTime)) {
      partyIdMap.put(partyId, room);
      log.info("방이 생성됨 : session.getId={}, RoomId={}", session.getId(), partyId);
      return;
    }

    log.warn("방이 생성되지 않음 : session.getId={}, RoomId={}", session.getId(), partyId);
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

      // 파티 채팅방 생성
      createChatRoom(session, room, partyId);

      // 파티에 사용자 등록
      joinChatRoom(session, partyId, room);

      log.info(
          "세션 추가됨: {}, 방: {}, 참가자수 : {} ", session.getId(), partyId, room.getSessions().size());
      log.info("세션 연결됨: sessionId={}, partyID={}", session.getId(), partyId);

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
    Chat chat = objectMapper.readValue(payload, Chat.class);
    if (chat.getId() != null) {
      chatMapper.insertChat(chat);
    }

    ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

    // 메시지 처리
    room.handleActions(session, chatMessage, chatService);
  }

  // 연결 종료 시 세션 제거
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    ChatRoom room = sessionRoomMap.remove(session);

    if (room != null) {
      // ChatRoom 에서 세션 제거 메서드
      room.removeSession(session);
    }
    log.info("세션 종료: sessionId={}, status={}", session.getId(), status);
  }
}
