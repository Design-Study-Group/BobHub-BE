package com.bobhub.party.service;

import com.bobhub.party.domain.Party;
import com.bobhub.party.domain.PartyCategory;
import com.bobhub.party.dto.PartyCreateRequest;
import com.bobhub.party.dto.PartyUpdateRequest;
import com.bobhub.party.dto.PartyUpdateResponse;
import com.bobhub.party.dto.PartyViewResponse;
import com.bobhub.party.mapper.PartyMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyService {

  private final PartyMapper partyMapper;
  private final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  /* 카테고리 별 파티 정보 조회 */
  public List<PartyViewResponse> getPartiesByCategory(String category) {
    List<PartyViewResponse> parties = partyMapper.getPartiesByCategory(category);
    LocalDateTime now = LocalDateTime.now();
    for (PartyViewResponse party : parties) {
      if (party.getFinishedAt() != null && party.getFinishedAt().isBefore(now)) {
        party.setIsOpen(false);
      } else {
        party.setIsOpen(true);
      }
    }
    return parties;
  }

  /* 파티원 모집 */
  public void createParty(PartyCreateRequest request) {
    System.out.println("createParty: " + request.getFinishedAt());
    LocalDateTime finishedAt = LocalDateTime.parse(request.getFinishedAt(), dateTimeFormatter);
    PartyCategory partyCategory = PartyCategory.valueOf(request.getCategory().toUpperCase());
    int limitPrice = request.getLimitPrice() != null ? request.getLimitPrice() : 0;

    Party party =
        Party.builder()
            .title(request.getTitle())
            .limitPeople(Integer.parseInt(request.getLimitPeople()))
            .limitPrice(limitPrice)
            .ownerId(request.getOwnerId())
            .finishedAt(finishedAt)
            .category(partyCategory)
            .isOpen(true)
            .build();

    partyMapper.createParty(party);
  }

  /* 파티 상세정보 조회 */
  public PartyUpdateResponse getPartyById(Long partyId) {
    try {
      Party party = partyMapper.getPartyById(partyId);
      if (party == null) {
        return PartyUpdateResponse.builder().message("파티를 찾을 수 없습니다.").success(false).build();
      }

      return PartyUpdateResponse.builder()
          .party(party)
          .message("파티 정보를 성공적으로 조회했습니다.")
          .success(true)
          .build();
    } catch (Exception e) {
      return PartyUpdateResponse.builder().message("파티 조회 중 오류가 발생했습니다.").success(false).build();
    }
  }

  /* 파티 정보 수정 */
  @Transactional
  public PartyUpdateResponse updateParty(PartyUpdateRequest request, Long userId) {
    try {
      // 파티 존재 여부 확인
      Party existingParty = partyMapper.getPartyById(request.getId());
      if (existingParty == null) {
        return PartyUpdateResponse.builder().message("파티를 찾을 수 없습니다.").success(false).build();
      }

      // 파티 소유자 확인
      if (!partyMapper.isPartyOwner(request.getId(), userId)) {
        return PartyUpdateResponse.builder().message("파티를 수정할 권한이 없습니다.").success(false).build();
      }

      // 파티 정보 업데이트
      Party updatedParty =
          Party.builder()
              .id(request.getId())
              .title(request.getTitle() != null ? request.getTitle() : existingParty.getTitle())
              .category(
                  request.getCategory() != null
                      ? request.getCategory()
                      : existingParty.getCategory())
              .limitPeople(
                  request.getLimitPeople() != null
                      ? request.getLimitPeople()
                      : existingParty.getLimitPeople())
              .limitPrice(
                  request.getLimitPrice() != null
                      ? request.getLimitPrice()
                      : existingParty.getLimitPrice())
              .finishedAt(
                  request.getFinishedAt() != null
                      ? request.getFinishedAt()
                      : existingParty.getFinishedAt())
              .isOpen(request.getIsOpen() != null ? request.getIsOpen() : existingParty.getIsOpen())
              .ownerId(existingParty.getOwnerId())
              .createdAt(existingParty.getCreatedAt())
              .build();

      partyMapper.updateParty(updatedParty);

      return PartyUpdateResponse.builder()
          .party(updatedParty)
          .message("파티 정보를 성공적으로 수정했습니다.")
          .success(true)
          .build();
    } catch (Exception e) {
      return PartyUpdateResponse.builder().message("파티 수정 중 오류가 발생했습니다.").success(false).build();
    }
  }

  /* 파티글 삭제 */
  @Transactional
  public PartyUpdateResponse deleteParty(Long partyId, Long userId) {
    try {
      // 파티 존재 여부 확인
      Party existingParty = partyMapper.getPartyById(partyId);
      if (existingParty == null) {
        return PartyUpdateResponse.builder().message("파티를 찾을 수 없습니다.").success(false).build();
      }

      // 파티 소유자 확인
      if (!partyMapper.isPartyOwner(partyId, userId)) {
        return PartyUpdateResponse.builder().message("파티를 삭제할 권한이 없습니다.").success(false).build();
      }

      partyMapper.deleteParty(partyId);

      return PartyUpdateResponse.builder().message("파티를 성공적으로 삭제했습니다.").success(true).build();
    } catch (Exception e) {
      return PartyUpdateResponse.builder().message("파티 삭제 중 오류가 발생했습니다.").success(false).build();
    }
  }

  /* 파티 소유자 확인 */
  public boolean isPartyOwner(Long partyId, Long userId) {
    return partyMapper.isPartyOwner(partyId, userId);
  }

  /* 파티 조인 */

}
