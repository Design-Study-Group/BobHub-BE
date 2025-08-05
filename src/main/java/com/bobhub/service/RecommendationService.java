package com.bobhub.service;

import com.bobhub.Repository.RecommendationRepository;
import com.bobhub.domain.Recommendation;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public void save(Recommendation recommendation) {
        recommendationRepository.save(recommendation);
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

    public void update(Recommendation boardDto) {
        recommendationRepository.update(boardDto);
    }

    public void delete(Long id) {
        recommendationRepository.delete(id);
    }
}