package com.bobhub.party.controller;

import com.bobhub.chat.service.ChatService;
import com.bobhub.party.dto.PartyCreateRequest;
import com.bobhub.party.dto.PartyUpdateRequest;
import com.bobhub.party.dto.PartyUpdateResponse;
import com.bobhub.party.dto.PartyViewResponse;
import com.bobhub.party.service.PartyService;
import com.bobhub.user.mapper.UserMapper;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parties")
@RequiredArgsConstructor
public class PartyController {
  private final PartyService partyService;
  private final UserMapper userMapper;
  private final ChatService chatService;

  @GetMapping
  public List<PartyViewResponse> viewPartiesByCategory(
      @RequestParam(defaultValue = "DELIVERY") String category, Principal principal) {

    System.out.println("viewPartiesByCategory: " + category);
    String upperCategory = category.toUpperCase();
    return partyService.getPartiesByCategory(upperCategory);
  }

  @PostMapping
  public int createParties(
      Principal principal, @RequestBody PartyCreateRequest request, Model model) {
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
      model.addAttribute(
          "errors", List.of(new ObjectError("limitPeople", "최대 인원 수는 정수만 입력할 수 있습니다.")));
      return -1;
    }
    // 파티 생성 서비스에 int로 넘기기 위해 setter 추가 필요시 수정
    request.setLimitPeople(String.valueOf(limitPeopleInt));

    System.out.println("create" + request.toString());
    return 0;
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

  @PostMapping("/{id}/join")
  public ResponseEntity<Map<String, Object>> joinParty(
      @PathVariable("id") Long partyId, Principal principal) {
    Long userId = getUserIdFromPrincipal(principal);
    try {
      String msg = partyService.createJoinParty(partyId, userId);
      Map<String, Object> map = new HashMap<>();
      map.put("message", msg);
      map.put("success", true);

      return ResponseEntity.ok(map);
    } catch (Exception e) {
      Map<String, Object> response = new HashMap<>();
      response.put("message", "파티 참여에 실패했습니다: " + e.getMessage());
      response.put("success", false);

      return ResponseEntity.badRequest().body(response);
    }
  }
}
