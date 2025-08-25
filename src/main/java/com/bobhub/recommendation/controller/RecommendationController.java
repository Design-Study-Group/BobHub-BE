package com.bobhub.recommendation.controller;

import com.bobhub.recommendation.domain.Recommendation;
import com.bobhub.recommendation.repository.RecommendationRepository;
import com.bobhub.recommendation.service.RecommendationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RecommendationController {
  private final RecommendationService recommendationService;
  private final RecommendationRepository recommendationRepository;

  //    private final RecommendationService recommendationService;

  @GetMapping("/recommendation/save")
  public String save() {
    return "recommendation_save";
  }

  @PostMapping("/recommendation/save")
  public String save(Recommendation recommendation) {
    recommendationService.save(recommendation);
    return "redirect:/recommendation";
  }

  @GetMapping("/recommendation")
  public String findAll(Model model) {
    List<Recommendation> recommendationList = recommendationService.findAll();
    model.addAttribute("recommendationList", recommendationList);
    return "recommendation_list";
  }

  @GetMapping("/recommendation/{id}")
  public String findById(@PathVariable("id") Long id, Model model) {
    // 조회수 처리
    //        recommendationService.updateHits(id);

    // 상세내용 가져옴
    Recommendation recommendation = recommendationService.findById(id);
    model.addAttribute("recommendation", recommendation);

    //        List<CommentDto> commentDtoList = commentService.findAll(id);
    //        model.addAttribute("commentDtoList", commentDtoList);
    return "recommendation_detail";
  }

  @PostMapping("/recommendation/update")
  public String update(@ModelAttribute Recommendation recommendation) {
    recommendationService.update(recommendation);
    return "redirect:/recommendation";
  }

  @GetMapping("/recommendation/update/{id}")
  public String update(@PathVariable("id") Long id, Model model) {
    Recommendation recommendation = recommendationService.findById(id);
    model.addAttribute("recommendation", recommendation);
    return "recommendation_update";
  }

  @GetMapping("/recommendation/delete/{id}")
  public String delete(@PathVariable("id") Long id) {
    recommendationService.delete(id);
    return "redirect:/recommendation";
  }
}
