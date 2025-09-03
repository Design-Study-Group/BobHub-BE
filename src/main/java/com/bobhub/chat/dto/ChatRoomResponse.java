package com.bobhub.chat.dto;

import lombok.Data;

@Data
public class ChatRoomResponse {
    private long partyId;
    private int participantCount;

    public ChatRoomResponse(ChatRoom room) {
        this.partyId = room.getPartyId();
        this.participantCount = room.getSessions().size();
    }
}
