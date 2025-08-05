package com.bobhub.controller;

import com.bobhub.dto.PartyCreateRequest;
import com.bobhub.dto.PartyUpdateRequest;
import com.bobhub.dto.PartyUpdateResponse;
import com.bobhub.dto.PartyViewResponse;
import com.bobhub.mapper.UserMapper;
import com.bobhub.service.PartyService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Controller
@RequestMapping("/parties")
@RequiredArgsConstructor
public class PartyController {
  private final PartyService partyService;
  private final UserMapper userMapper;

  @GetMapping
  public String viewPartiesByCategory(
      @RequestParam(defaultValue = "DELIVERY") String category, Model model, Principal principal) {
    String upperCategory = category.toUpperCase();
    List<PartyViewResponse> parties = partyService.getPartiesByCategory(upperCategory);

    model.addAttribute("category", upperCategory);
    model.addAttribute("parties", parties);

    if (principal != null) {
      var user = userMapper.findByEmail(principal.getName());
      if (user != null) {
        model.addAttribute("userId", user.getId());
      }
    }
    return "parties";
  }

  @PostMapping("/create")
  public String createParties(
      Principal principal,
      @ModelAttribute PartyCreateRequest request,
      Model model) {
    String email = null;
    if (principal instanceof OAuth2AuthenticationToken) {
      OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
      email = (String) oauthToken.getPrincipal().getAttributes().get("email");
    } else {
      email = principal.getName(); // 일반 로그인 등일 경우
    }

    if (email == null) {
      throw new IllegalStateException("로그인 정보에 이메일이 없습니다.");
    }

    Long userId = userMapper.findByEmail(email).getId();
    request.setOwnerId(userId);

    // limitPeople 정수 변환 및 검증
    int limitPeopleInt;
    try {
      double doubleValue = Double.parseDouble(request.getLimitPeople());
      if (doubleValue % 1 != 0) {
        throw new NumberFormatException();
      }
      limitPeopleInt = (int) doubleValue;
    } catch (Exception e) {
      model.addAttribute("errors", List.of(new ObjectError("limitPeople", "최대 인원 수는 정수만 입력할 수 있습니다.")));
      return "parties";
    }
    // 파티 생성 서비스에 int로 넘기기 위해 setter 추가 필요시 수정
    request.setLimitPeople(String.valueOf(limitPeopleInt));

    partyService.createParty(request);
    return "redirect:/parties";
  }

  @GetMapping("/edit/{partyId}")
  public String editPartyForm(@PathVariable Long partyId, Model model, Principal principal) {
    PartyUpdateResponse response = partyService.getPartyById(partyId);
    if (!response.isSuccess()) {
      return "redirect:/parties?error=파티를 찾을 수 없습니다.";
    }

    Long userId = getUserIdFromPrincipal(principal);
    if (userId == null) {
      return "redirect:/parties?error=로그인이 필요합니다.";
    }

    if (!partyService.isPartyOwner(partyId, userId)) {
      return "redirect:/parties?error=파티를 수정할 권한이 없습니다.";
    }

    model.addAttribute("party", response.getParty());
    model.addAttribute("userId", userId);
    return "edit-party";
  }

  @PostMapping("/edit/{partyId}")
  public String updateParty(
      @PathVariable Long partyId,
      @ModelAttribute PartyUpdateRequest request,
      BindingResult bindingResult,
      Principal principal,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("errors", bindingResult.getAllErrors());
      model.addAttribute("party", request);
      return "edit-party";
    }

    // 사용자 ID 가져오기
    Long userId = getUserIdFromPrincipal(principal);
    if (userId == null) {
      return "redirect:/parties?error=로그인이 필요합니다.";
    }

    request.setId(partyId);
    PartyUpdateResponse response = partyService.updateParty(request, userId);

    if (response.isSuccess()) {
      return "redirect:/parties?success=파티 정보가 성공적으로 수정되었습니다.";
    } else {
      return "redirect:/parties/edit/" + partyId + "?error=" + response.getMessage();
    }
  }

  @PostMapping("/delete/{partyId}")
  public String deleteParty(@PathVariable Long partyId, Principal principal) {

    // 사용자 ID 가져오기
    Long userId = getUserIdFromPrincipal(principal);
    if (userId == null) {
      return "redirect:/parties?error=로그인이 필요합니다.";
    }

    PartyUpdateResponse response = partyService.deleteParty(partyId, userId);

    if (response.isSuccess()) {
      return "redirect:/parties?success=파티가 성공적으로 삭제되었습니다.";
    } else {
      return "redirect:/parties?error=" + response.getMessage();
    }
  }

  private Long getUserIdFromPrincipal(Principal principal) {
    if (principal == null) {
      return null;
    }

    String email = null;
    if (principal instanceof OAuth2AuthenticationToken) {
      OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
      email = (String) oauthToken.getPrincipal().getAttributes().get("email");
    } else {
      email = principal.getName();
    }

    if (email == null) {
      return null;
    }

    var user = userMapper.findByEmail(email);
    return user != null ? user.getId() : null;
  }
}
