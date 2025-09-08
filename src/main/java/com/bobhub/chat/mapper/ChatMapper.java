package com.bobhub.chat.mapper;

import com.bobhub.chat.domain.Chat;
import com.bobhub.chat.dto.ChatMessageResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMapper {
  List<ChatMessageResponse> getChatsByPartyId(@Param("partyId") long partyId);

  void insertChat(Chat chat);
}
