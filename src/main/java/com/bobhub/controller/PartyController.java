package com.bobhub.controller;

import com.bobhub.dto.PartyCreateRequest;
import com.bobhub.dto.PartyViewResponse;
import com.bobhub.mapper.UserMapper;
import com.bobhub.service.PartyService;
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
    public String createParties(@ModelAttribute PartyCreateRequest request) {
        partyService.createParty(request);
        return "redirect:/parties";
    }
}
