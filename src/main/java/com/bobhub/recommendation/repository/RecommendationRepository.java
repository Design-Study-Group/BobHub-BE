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

  public void save(Recommendation recommendation) {
    recommendationMapper.insert(recommendation);
  }

  public List<Recommendation> findAll() {
    return recommendationMapper.findAll();
  }

  //  public void updateHits(Long id) {
  //  }

  public Recommendation findById(Long id) {
    return recommendationMapper.findById(id);
  }

  public void update(Recommendation recommendation) {
    recommendationMapper.update(recommendation);
  }

  public void delete(Long id) {
    recommendationMapper.delete(id);
  }
}
