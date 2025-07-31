package com.bobhub.dto;

import com.bobhub.domain.PartyCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PartyViewResponse {
    private long id;
    private PartyCategory category;
    private String title;
    private int limitPeople;
    private int limitPrice;
    private long ownerId;
    private boolean isOpen;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private int currentPeople;
}