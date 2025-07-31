package com.bobhub.service;

import com.bobhub.domain.Party;
import com.bobhub.domain.PartyCategory;
import com.bobhub.dto.PartyCreateRequest;
import com.bobhub.mapper.PartyMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PartyService {

    private final PartyMapper partyMapper;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public PartyService(PartyMapper partyMapper) {
        this.partyMapper = partyMapper;
    }

    public List<Party> getPartiesByCategory(String category) {
        List<Party> parties = partyMapper.getPartiesByCategory(category);
        for (Party party : parties) {
            if (party.getFinishedAt() != null && party.getFinishedAt().isBefore(LocalDate.now())) {
                party.setOpen(false);
            }
        }
        return parties;
    }

    public void createParty(PartyCreateRequest request) {
        LocalDateTime finishedAt = LocalDateTime.parse(request.getFinishedAt(), dateTimeFormatter);
        PartyCategory partyCategory = PartyCategory.valueOf(request.getCategory());
        long ownerId = Long.parseLong(request.getOwnerId());
        int limitPrice = request.getLimitPrice() != null ? request.getLimitPrice() : 0;

        Party party = Party.builder()
                            .title(request.getTitle())
                            .limitPeople(request.getLimitPeople())
                            .limitPrice(limitPrice)
                            .ownerId(ownerId)
                            .finishedAt(LocalDate.from(finishedAt))
                            .category(partyCategory)
                            .isOpen(true)
                            .build();

        partyMapper.createParty(party);
    }
}
