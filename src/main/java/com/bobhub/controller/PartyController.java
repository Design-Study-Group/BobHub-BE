package com.bobhub.controller;

import com.bobhub.dto.PartyCreateRequest;
import com.bobhub.dto.PartyViewResponse;
import com.bobhub.mapper.UserMapper;
import com.bobhub.service.PartyService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/parties")
public class PartyController {
    private final PartyService partyService;
    private final UserMapper userMapper;

    public PartyController(PartyService partyService, UserMapper userMapper) {
        this.partyService = partyService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String viewPartiesByCategory(@RequestParam(defaultValue = "DELIVERY") String category, Model model, Principal principal) {
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
    public String createParties(Principal principal, @ModelAttribute PartyCreateRequest request) {
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
        partyService.createParty(request);

        return "redirect:/parties";
    }

}
