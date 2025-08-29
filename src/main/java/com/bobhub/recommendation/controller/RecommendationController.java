package com.bobhub.recommendation.controller;

import com.bobhub._core.security.PrincipalDetails;
import com.bobhub.recommendation.domain.Recommendation;
import com.bobhub.recommendation.domain.RecommendationComment;
import com.bobhub.recommendation.dto.RecommendationCommentResponseDto;
import com.bobhub.recommendation.dto.RecommendationRequestDto;
import com.bobhub.recommendation.dto.RecommendationResponseDto;
import com.bobhub.recommendation.service.RecommendationCommentService;
import com.bobhub.recommendation.service.RecommendationService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

  private final RecommendationService recommendationService;
  private final RecommendationCommentService commentService;

  private long getUserIdFromAuthentication(Authentication authentication) {
    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
    return principalDetails.getUser().getId();
  }

  @PostMapping
  public ResponseEntity<RecommendationResponseDto> save(
      @RequestBody RecommendationRequestDto requestDto, Authentication authentication) {
    long userId = getUserIdFromAuthentication(authentication);
    Recommendation recommendation = requestDto.toEntity();
    recommendation.setUserId(userId);
    Recommendation savedRecommendation = recommendationService.save(recommendation);
    return new ResponseEntity<>(
        new RecommendationResponseDto(savedRecommendation), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<RecommendationResponseDto>> findAll() {
    List<Recommendation> recommendationList = recommendationService.findAll();
    List<RecommendationResponseDto> responseDtoList =
        recommendationList.stream()
            .map(RecommendationResponseDto::new)
            .collect(Collectors.toList());
    return ResponseEntity.ok(responseDtoList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RecommendationResponseDto> findById(@PathVariable("id") Long id) {
    // 1. 추천 게시물 정보 조회
    Recommendation recommendation = recommendationService.findById(id);
    if (recommendation == null) {
      return ResponseEntity.notFound().build();
    }

    // 2. 해당 게시물의 댓글 목록 조회
    List<RecommendationComment> comments = commentService.findCommentsByRecommendationId(id);

    // 3. DTO 변환
    RecommendationResponseDto responseDto = new RecommendationResponseDto(recommendation);
    List<RecommendationCommentResponseDto> commentDtos =
        comments.stream().map(RecommendationCommentResponseDto::new).collect(Collectors.toList());

    // 4. 댓글 목록을 추천 게시물 DTO에 설정
    responseDto.setComments(commentDtos);

    return ResponseEntity.ok(responseDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RecommendationResponseDto> update(
      @PathVariable("id") Long id,
      @RequestBody RecommendationRequestDto requestDto,
      Authentication authentication) {
    long userId = getUserIdFromAuthentication(authentication);
    Recommendation recommendation = requestDto.toEntity();
    recommendation.setId(id);
    recommendation.setUserId(userId);
    Recommendation updatedRecommendation = recommendationService.update(recommendation);
    return ResponseEntity.ok(new RecommendationResponseDto(updatedRecommendation));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    recommendationService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
