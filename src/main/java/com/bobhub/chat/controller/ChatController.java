package com.bobhub.chat.controller;

import com.bobhub.chat.dto.ChatRoom;
import com.bobhub.chat.dto.ChatRoomResponse;
import com.bobhub.chat.service.ChatService;
import com.bobhub.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/parties/{partyId}/comments")
public class ChatController {
    private final ChatService chatService;
    private final UserMapper userMapper;

    @GetMapping
    public ChatRoomResponse chatRoom(@PathVariable long partyId) {

        ChatRoom chatRoom = chatService.findRoomById(partyId);
        return new ChatRoomResponse(chatRoom);
    }
}
