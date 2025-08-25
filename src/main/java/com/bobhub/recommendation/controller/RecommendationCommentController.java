package com.bobhub.recommendation.controller;

import com.bobhub._core.security.PrincipalDetails;
import com.bobhub.recommendation.domain.RecommendationComment;
import com.bobhub.recommendation.dto.RecommendationCommentRequestDto;
import com.bobhub.recommendation.dto.RecommendationCommentResponseDto;
import com.bobhub.recommendation.service.RecommendationCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendation/{recommendationId}/comments")
@RequiredArgsConstructor
public class RecommendationCommentController {

    private final RecommendationCommentService commentService;

    private long getUserIdFromAuthentication(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return principalDetails.getUser().getId();
    }

    @PostMapping
    public ResponseEntity<RecommendationCommentResponseDto> createComment(
            @PathVariable long recommendationId,
            @RequestBody RecommendationCommentRequestDto requestDto,
            Authentication authentication) {
        long userId = getUserIdFromAuthentication(authentication);
        RecommendationComment createdComment = commentService.createComment(recommendationId, userId, requestDto);
        return new ResponseEntity<>(new RecommendationCommentResponseDto(createdComment), HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable long recommendationId, // Not used, but good for URL structure
            @PathVariable long commentId,
            @RequestBody RecommendationCommentRequestDto requestDto,
            Authentication authentication) {
        long userId = getUserIdFromAuthentication(authentication);
        if (!commentService.isOwner(commentId, userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable long recommendationId, // Not used, but good for URL structure
            @PathVariable long commentId,
            Authentication authentication) {
        long userId = getUserIdFromAuthentication(authentication);
        if (!commentService.isOwner(commentId, userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
