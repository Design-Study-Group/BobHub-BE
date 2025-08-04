package com.bobhub.Repository;

import com.bobhub.domain.Recommendation;
import com.bobhub.mapper.RecommendationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendationRepository {
    private final RecommendationMapper recommendationMapper;
    private final SqlSessionTemplate sqlSesstionTemplate;

    public void save(Recommendation recommendation) {
        recommendationMapper.insert(recommendation);
//        sqlSesstionTemplate.insert("Recommendation.save", recommendation);
    }

    public List<Recommendation> findAll() {
        return recommendationMapper.findAll();
//        return sqlSesstionTemplate.selectList("Recommendation.findAll");
    }

//    public void updateHits(Long id) {
//        sqlSesstionTemplate.update("Recommendation.updateHits", id);
//    }

    public Recommendation findById(Long id) {
        return recommendationMapper.findById(id);
//        return sqlSesstionTemplate.selectOne("Recommendation.findById", id);
    }

    public void update(Recommendation recommendation) {
        recommendationMapper.update(recommendation);
//        sqlSesstionTemplate.update("Recommendation.update", recommendation);
    }

    public void delete(Long id) {
        recommendationMapper.delete(id);
//        sqlSesstionTemplate.delete("Recommendation.delete", id);
    }
}
