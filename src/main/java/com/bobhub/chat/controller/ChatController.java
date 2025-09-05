package com.bobhub.chat.controller;

import com.bobhub.chat.domain.ChatRoom;
import com.bobhub.chat.dto.ChatMessageResponse;
import com.bobhub.chat.dto.ChatRoomResponse;
import com.bobhub.chat.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/parties/{partyId}/comments")
public class ChatController {
  private final ChatService chatService;

  @GetMapping
  public ChatRoomResponse chatRoom(@PathVariable long partyId) {
    ChatRoom chatRoom = chatService.findRoomById(partyId);
    return new ChatRoomResponse(chatRoom);
  }

  @GetMapping("/history")
  public List<ChatMessageResponse> chatRoomHistory(@PathVariable long partyId) {
    return chatService.getChatHistory(partyId);
  }
}
