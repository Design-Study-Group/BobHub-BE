package com.bobhub.recommendation.repository;

import com.bobhub.recommendation.domain.Recommendation;
import com.bobhub.recommendation.mapper.RecommendationMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecommendationRepository {
  private final RecommendationMapper recommendationMapper;

  public Recommendation save(Recommendation recommendation) {
    recommendationMapper.insert(recommendation);
    return recommendation;
  }

  public List<Recommendation> findAll() {
    return recommendationMapper.findAll();
  }

  //  public void updateHits(Long id) {
  //  }

  public Recommendation findById(Long id) {
    return recommendationMapper.findById(id);
  }

  public Recommendation update(Recommendation recommendation) {
    recommendationMapper.update(recommendation);
    return recommendation;
  }

  public void delete(Long id) {
    recommendationMapper.delete(id);
  }
}
