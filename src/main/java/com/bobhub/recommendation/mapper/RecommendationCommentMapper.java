package com.bobhub.recommendation.mapper;

import com.bobhub.recommendation.domain.RecommendationComment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendationCommentMapper {
  void insert(RecommendationComment comment);

  RecommendationComment findById(long id);

  List<RecommendationComment> findByRecommendationId(long recommendationId);

  void update(RecommendationComment comment);

  void delete(long id);
}
