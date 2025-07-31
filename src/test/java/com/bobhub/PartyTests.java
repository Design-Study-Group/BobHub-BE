package com.bobhub;

import com.bobhub.domain.Party;
import com.bobhub.domain.PartyCategory;
import com.bobhub.dto.PartyCreateRequest;
import com.bobhub.service.PartyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class PartyTests {

    @Autowired
    private PartyService partyService;

    @Test
    void createParty() {
        LocalDate now = LocalDate.now();
        PartyCreateRequest party = PartyCreateRequest.builder()
                .category("DELIVERY")
                .title("중복 맞이 치킨팟 🍗")
                .limitPeople(4)
                .limitPrice(40000)
                .ownerId("61")
                .isOpen(true)
                .finishedAt("2025-08-01T12:30")
                .build();

        partyService.createParty(party);
    }
}
