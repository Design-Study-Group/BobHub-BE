package com.bobhub.chatbot;

import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {
  private final ChatClient chatClient;

  @Value("classpath:/prompts/chat-prompt.st")
  private Resource chatPromptTemplate;

  public ChatbotService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public String getKoreanChatResponse(String message) {
    // 한국어 응답을 위한 프롬프트 템플릿 사용
    PromptTemplate promptTemplate = new PromptTemplate(chatPromptTemplate);
    Prompt prompt = promptTemplate.create(Map.of("message", message));

    // Gemini 모델에 요청 보내기
    String response = chatClient.prompt(prompt).call().content();

    return response;
  }
}
