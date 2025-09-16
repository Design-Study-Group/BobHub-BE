package com.bobhub.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatbotService {
  private final ChatClient chatClient;

  @Value("classpath:/prompts/chat-prompt.st")
  private Resource chatPromptTemplate;

  private final ConcurrentLinkedQueue<Request> requestQueue = new ConcurrentLinkedQueue<>();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final ExecutorService aiCallExecutor = Executors.newFixedThreadPool(5);
  private volatile boolean batchProcessingScheduled = false;
  private final long BATCH_DELAY_SECONDS = 1;

  private static class Request {
      String message;
      CompletableFuture<String> future;

      public Request(String message, CompletableFuture<String> future) {
          this.message = message;
          this.future = future;
      }

      public String getMessage() { return message; }
      public CompletableFuture<String> getFuture() { return future; }
  }

  public ChatbotService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @PreDestroy
  public void shutdown() {
      scheduler.shutdown();
      aiCallExecutor.shutdown();
      try {
          if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
              scheduler.shutdownNow();
          }
          if (!aiCallExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
              aiCallExecutor.shutdownNow();
          }
      } catch (InterruptedException e) {
          scheduler.shutdownNow();
          aiCallExecutor.shutdownNow();
          Thread.currentThread().interrupt();
      }
  }

  public CompletableFuture<String> getKoreanChatResponse(String message) {
    CompletableFuture<String> future = new CompletableFuture<>();
    requestQueue.offer(new Request(message, future));

    synchronized (this) {
        if (!batchProcessingScheduled) {
            scheduler.schedule(this::processBatch, BATCH_DELAY_SECONDS, TimeUnit.SECONDS);
            batchProcessingScheduled = true;
        }
    }
    return future;
  }

  private void processBatch() {
      List<Request> currentBatch = new ArrayList<>();
      Request req;
      while ((req = requestQueue.poll()) != null) {
          currentBatch.add(req);
      }

      if (currentBatch.isEmpty()) {
          synchronized (this) {
              batchProcessingScheduled = false;
          }
          return;
      }

      for (Request request : currentBatch) {
          CompletableFuture.supplyAsync(() -> {
              try {
                  PromptTemplate promptTemplate = new PromptTemplate(chatPromptTemplate);
                  Prompt prompt = promptTemplate.create(Map.of("message", request.getMessage()));
                  return chatClient.prompt(prompt).call().content();
              } catch (Exception e) {
                  log.error("Error during AI call for message '{}': {}", request.getMessage(), e.getMessage());
                  throw new RuntimeException("AI processing failed", e);
              }
          }, aiCallExecutor)
          .whenComplete((response, throwable) -> {
              if (throwable != null) {
                  request.getFuture().completeExceptionally(throwable);
              } else {
                  request.getFuture().complete(response);
              }
          });
      }

      synchronized (this) {
          batchProcessingScheduled = false;
      }
  }
}
