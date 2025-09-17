package com.bobhub.chatbot;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatbotWebSocketController {

  private final ChatbotService chatbotService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/chatbot")
  public void handleChatbotMessage(String message, SimpMessageHeaderAccessor headerAccessor) {
    String sessionId = headerAccessor.getSessionId();
    CompletableFuture<String> futureResponse = chatbotService.getKoreanChatResponse(message);

    futureResponse
        .thenAccept(
            response -> {
              messagingTemplate.convertAndSendToUser(sessionId, "/queue/messages", response);
            })
        .exceptionally(
            ex -> {
              messagingTemplate.convertAndSendToUser(
                  sessionId,
                  "/queue/messages",
                  "Error processing your request: " + ex.getMessage());
              return null;
            });
  }
}
