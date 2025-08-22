package com.bobhub.user.service;

import com.bobhub.party.domain.Party;
import com.bobhub.party.domain.PartyMember;
import com.bobhub.user.domain.User;
import com.bobhub.party.mapper.PartyMapper;
import com.bobhub.party.mapper.PartyMemberMapper;
import com.bobhub.user.mapper.UserMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageService {
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
    for (PartyMember partyMember : partyMemberMapper.findByUserId(user.getId())) {
      parties.add(partyMapper.getPartyById(partyMember.getId()));
    }

    return parties;
  }
}
