package com.bobhub.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatbotWebSocketController {

  private final ChatbotService chatbotService;

  @MessageMapping("/chatbot")
  @SendTo("/topic/messages")
  public String handleChatbotMessage(String message) {
    // Gemini AI를 사용하여 응답 생성
    return chatbotService.getKoreanChatResponse(message);
  }
}
