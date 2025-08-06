package com.bobhub.service;

import com.bobhub.domain.Party;
import com.bobhub.domain.PartyCategory;
import com.bobhub.domain.PartyMember;
import com.bobhub.domain.User;
import com.bobhub.mapper.PartyMapper;
import com.bobhub.mapper.PartyMemberMapper;
import com.bobhub.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageService {
  private final PartyService partyService;
  private final OAuth2UserService oAuth2UserService;
  private final UserMapper userMapper;
  private final PartyMapper partyMapper;
  private final PartyMemberMapper partyMemberMapper;

  public List<Party> getPartiesByOAuth(OAuth2User oAuth2User) {
    String email = oAuth2User.getAttribute("email");
    User user = userMapper.findByEmail(email);

    return partyMapper.getAllPartyByOwnerId(user.getId());
  }

  public List<Party> getMyParticipantPartiesByOAuth(OAuth2User oAuth2User) {
    String email = oAuth2User.getAttribute("email");
    User user = userMapper.findByEmail(email);

    List<Party> parties = new ArrayList<>();
    for(PartyMember partyMember : partyMemberMapper.findByUserId(user.getId())){
      parties.add(partyMapper.getPartyById(partyMember.getId()));
    }

    return parties;
  }
}
