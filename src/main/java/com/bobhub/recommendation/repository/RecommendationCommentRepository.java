package com.bobhub.recommendation.repository;

import com.bobhub.recommendation.domain.RecommendationComment;
import com.bobhub.recommendation.mapper.RecommendationCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendationCommentRepository {

    private final RecommendationCommentMapper commentMapper;

    public RecommendationComment save(RecommendationComment comment) {
        commentMapper.insert(comment);
        return comment;
    }

    public RecommendationComment findById(long id) {
        return commentMapper.findById(id);
    }

    public List<RecommendationComment> findByRecommendationId(long recommendationId) {
        return commentMapper.findByRecommendationId(recommendationId);
    }

    public void update(RecommendationComment comment) {
        commentMapper.update(comment);
    }

    public void delete(long id) {
        commentMapper.delete(id);
    }
}
