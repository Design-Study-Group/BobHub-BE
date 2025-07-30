package com.bobhub.controller;

import com.bobhub.service.PartyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PartyController {
    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping("/parties")
    public String viewPartiesByCategory(@RequestParam(defaultValue = "배달") String category, Model model) {
        model.addAttribute("category", category);
        model.addAttribute("parties", partyService.getPartiesByCategory(category));
        return "parties";
    }
}
