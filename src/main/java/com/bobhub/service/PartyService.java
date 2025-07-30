package com.bobhub.service;

import com.bobhub.domain.Party;
import com.bobhub.mapper.PartyMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PartyService {

    private final PartyMapper partyMapper;

    public PartyService(PartyMapper partyMapper) {
        this.partyMapper = partyMapper;
    }

    public List<Party> getPartiesByCategory(String category) {
        List<Party> parties = partyMapper.getPartiesByCategory(category);
        Date now = new Date();
        for (Party party : parties) {
            if (party.getFinishedAt() != null && party.getFinishedAt().before(now)) {
                party.setOpen(false);
            }
        }
        return parties;
    }
}
