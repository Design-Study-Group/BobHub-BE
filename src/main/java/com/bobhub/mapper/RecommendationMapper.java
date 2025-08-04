package com.bobhub.mapper;

import com.bobhub.domain.Recommendation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommendationMapper {
    void insert(Recommendation recommendation);
    List<Recommendation> findAll();
    Recommendation findById(Long id);
    void update(Recommendation recommendation);
    void delete(Long id);
}
