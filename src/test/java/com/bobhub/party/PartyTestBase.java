package com.bobhub.party;

import com.bobhub.domain.Party;
import com.bobhub.domain.PartyCategory;
import com.bobhub.dto.PartyCreateRequest;
import com.bobhub.dto.PartyUpdateRequest;
import com.bobhub.service.PartyService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/** 파티 테스트를 위한 베이스 클래스 공통 테스트 데이터와 헬퍼 메서드를 제공합니다. */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class PartyTestBase {

  @Autowired protected PartyService partyService;

  protected static final Long TEST_OWNER_ID = 61L;
  protected static final Long TEST_NON_OWNER_ID = 999L;
  protected static final Long NON_EXISTENT_PARTY_ID = 99999L;

  protected static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  @BeforeEach
  void setUp() {
    // 각 테스트 전에 실행될 공통 설정
    setupTestData();
  }

  /** 테스트 데이터 설정 */
  protected void setupTestData() {
    // 필요한 경우 테스트 데이터를 미리 생성
  }

  /** 테스트용 파티 생성 요청 생성 */
  protected PartyCreateRequest createTestPartyRequest() {
    return PartyCreateRequest.builder()
        .category("DELIVERY")
        .title("테스트 파티")
        .limitPeople(String.valueOf(4))
        .limitPrice(30000)
        .ownerId(TEST_OWNER_ID)
        .isOpen(true)
        .finishedAt(LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER))
        .build();
  }

  /** 테스트용 파티 수정 요청 생성 */
  protected PartyUpdateRequest createTestUpdateRequest(Long partyId) {
    return PartyUpdateRequest.builder()
        .id(partyId)
        .title("수정된 테스트 파티")
        .category(PartyCategory.DINE_OUT)
        .limitPeople(6)
        .limitPrice(50000)
        .finishedAt(LocalDateTime.now().plusDays(2))
        .isOpen(false)
        .build();
  }

  /** 부분 수정용 파티 수정 요청 생성 */
  protected PartyUpdateRequest createPartialUpdateRequest(Long partyId, String newTitle) {
    return PartyUpdateRequest.builder()
        .id(partyId)
        .title(newTitle)
        // 다른 필드들은 null로 두어 기존 값 유지
        .build();
  }

  /** 테스트용 파티 엔티티 생성 */
  protected Party createTestParty() {
    return Party.builder()
        .title("테스트 파티")
        .category(PartyCategory.DELIVERY)
        .limitPeople(4)
        .limitPrice(30000)
        .ownerId(TEST_OWNER_ID)
        .isOpen(true)
        .finishedAt(LocalDateTime.now().plusDays(1))
        .createdAt(LocalDateTime.now())
        .build();
  }

  /** 테스트 결과 검증 헬퍼 메서드 */
  protected void assertSuccessResponse(com.bobhub.dto.PartyUpdateResponse response) {
    assert response.isSuccess();
    assert response.getMessage() != null;
    assert !response.getMessage().isEmpty();
  }

  protected void assertFailureResponse(com.bobhub.dto.PartyUpdateResponse response) {
    assert !response.isSuccess();
    assert response.getMessage() != null;
    assert !response.getMessage().isEmpty();
  }

  protected void assertPartyFields(
      Party party,
      String expectedTitle,
      PartyCategory expectedCategory,
      int expectedLimitPeople,
      int expectedLimitPrice,
      boolean expectedIsOpen) {
    assert party.getTitle().equals(expectedTitle);
    assert party.getCategory() == expectedCategory;
    assert party.getLimitPeople() == expectedLimitPeople;
    assert party.getLimitPrice() == expectedLimitPrice;
    assert party.getIsOpen() == expectedIsOpen;
  }
}
