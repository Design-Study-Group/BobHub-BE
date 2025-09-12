package com.bobhub._core.config;

import com.google.cloud.vertexai.VertexAI;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

  @Value("${spring.ai.vertexai.gemini.project-id}")
  private String projectId;

  @Value("${spring.ai.vertexai.gemini.location}")
  private String location;

  @Value("${spring.ai.vertexai.gemini.chat.options.model}")
  private String model;

  @Bean
  public VertexAI vertexAI() {
    return new VertexAI(projectId, location);
  }

  @Bean
  public VertexAiGeminiChatModel vertexAiGeminiChatModel(VertexAI vertexAI) {
    return new VertexAiGeminiChatModel(
        vertexAI, VertexAiGeminiChatOptions.builder().model(model).build());
  }

  @Bean
  public ChatClient chatClient(VertexAiGeminiChatModel vertexAiGeminiChatModel) {
    return ChatClient.builder(vertexAiGeminiChatModel).build();
  }
}
