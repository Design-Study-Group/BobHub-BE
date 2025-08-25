package com.bobhub.recommendation.dto;

import com.bobhub.recommendation.domain.Recommendation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RecommendationResponseDto {
    private long id;
    private long userId;
    private String category;
    private String storeName;
    private int star;
    private int totalTime;
    private int pricePerPerson;
    private boolean isReservation;
    private List<RecommendationCommentResponseDto> comments;

    public RecommendationResponseDto(Recommendation recommendation) {
        this.id = recommendation.getId();
        this.userId = recommendation.getUserId();
        this.category = recommendation.getCategory();
        this.storeName = recommendation.getStoreName();
        this.star = recommendation.getStar();
        this.totalTime = recommendation.getTotalTime();
        this.pricePerPerson = recommendation.getPricePerPerson();
        this.isReservation = recommendation.isReservation();
    }
}
