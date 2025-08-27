package com.bobhub.recommendation.service;

import com.bobhub.recommendation.domain.Recommendation;
import com.bobhub.recommendation.repository.RecommendationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {
  private final RecommendationRepository recommendationRepository;

  public Recommendation save(Recommendation recommendation) {
    return recommendationRepository.save(recommendation);
  }

  public List<Recommendation> findAll() {
    return recommendationRepository.findAll();
  }

  //  public void updateHits(Long id) {
  //      recommendationRepository.updateHits(id);
  //  }

  public Recommendation findById(Long id) {
    return recommendationRepository.findById(id);
  }

  public Recommendation update(Recommendation recommendation) {
    return recommendationRepository.update(recommendation);
  }

  public void delete(Long id) {
    recommendationRepository.delete(id);
  }
}
