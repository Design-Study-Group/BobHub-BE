package com.bobhub.controller;

import com.bobhub.domain.Party;
import com.bobhub.log.AllLogger;
import com.bobhub.service.MypageService;
import java.util.List;
import lombok.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
  private final MypageService mypageService;
  private final AllLogger allLogger;

  @GetMapping("")
  public String mypage() {
    return "mypage";
  }

  @GetMapping("/parties")
  public String mypage_parties(
      @RequestParam(defaultValue = "my") String category,
      @AuthenticationPrincipal OAuth2User oAuth2User,
      Model model) {
    List<Party> parties;

    if ("my".equals(category)) {
      parties = mypageService.getPartiesByOAuth(oAuth2User);
    } else if ("your".equals(category)) {
      parties = mypageService.getMyParticipantPartiesByOAuth(oAuth2User);
    } else {
      parties = List.of();
    }

    model.addAttribute("category", category);
    model.addAttribute("parties", parties);

    return "mypage_parties";
  }
}
