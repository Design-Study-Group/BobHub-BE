package com.bobhub.chat.dto;

import com.bobhub.chat.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private final long partyId;
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(long partyId) {
        this.partyId = partyId;
    }

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getMessage() + "님이 입장하셨습니다.");
        }
        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T data, ChatService chatService) {
        System.out.println("=== Sending message ===");
        System.out.println("Active sessions: " + sessions.size());
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, data));
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    // 세션 제거
    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }
}
