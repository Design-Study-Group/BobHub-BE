package com.bobhub.recommendation.service;

import com.bobhub.recommendation.domain.RecommendationComment;
import com.bobhub.recommendation.dto.RecommendationCommentRequestDto;
import com.bobhub.recommendation.mapper.RecommendationMapper;
import com.bobhub.recommendation.repository.RecommendationCommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationCommentService {

  private final RecommendationCommentRepository commentRepository;
  private final RecommendationMapper recommendationMapper;

  public RecommendationComment createComment(
      long recommendationId, long userId, RecommendationCommentRequestDto requestDto) {
    RecommendationComment comment = requestDto.toEntity(recommendationId, userId);
    RecommendationComment savedComment = commentRepository.save(comment);
    recommendationMapper.updateRating(recommendationId);
    return savedComment;
  }

  @Transactional(readOnly = true)
  public List<RecommendationComment> findCommentsByRecommendationId(long recommendationId) {
    return commentRepository.findByRecommendationId(recommendationId);
  }

  public void updateComment(long commentId, RecommendationCommentRequestDto requestDto) {
    RecommendationComment comment = commentRepository.findById(commentId);
    if (comment == null) {
      throw new IllegalArgumentException("Comment not found with id: " + commentId);
    }
    comment.setContent(requestDto.getContent());
    comment.setStar(requestDto.getStar());
    commentRepository.update(comment);
    recommendationMapper.updateRating(comment.getRecommendationId());
  }

  public void deleteComment(long commentId) {
    RecommendationComment comment = commentRepository.findById(commentId);
    if (comment != null) {
      commentRepository.delete(commentId);
      recommendationMapper.updateRating(comment.getRecommendationId());
    }
  }

  @Transactional(readOnly = true)
  public boolean isOwner(long commentId, long userId) {
    RecommendationComment comment = commentRepository.findById(commentId);
    return comment != null && comment.getUserId() == userId;
  }
}
