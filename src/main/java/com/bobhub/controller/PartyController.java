package com.bobhub.controller;

import com.bobhub.domain.Party;
import com.bobhub.dto.PartyCreateRequest;
import com.bobhub.mapper.UserMapper;
import com.bobhub.service.PartyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public String viewPartiesByCategory(@RequestParam(defaultValue = "delivery") String category, Model model, Principal principal) {
        model.addAttribute("category", category.toUpperCase());
        model.addAttribute("parties", partyService.getPartiesByCategory(category.toUpperCase()));
        if (principal != null) {
            model.addAttribute("userId", userMapper.findByEmail(principal.getName()));
        }
        return "parties";
    }

    @PostMapping("/create")
    public String createParties(@ModelAttribute PartyCreateRequest request) {
        partyService.createParty(request);
        return "redirect:/parties";
    }
}
