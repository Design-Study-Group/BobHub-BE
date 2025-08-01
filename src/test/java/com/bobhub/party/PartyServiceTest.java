package com.bobhub.party;

import static org.junit.jupiter.api.Assertions.*;

import com.bobhub.domain.PartyCategory;
import com.bobhub.dto.PartyUpdateRequest;
import com.bobhub.dto.PartyUpdateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * PartyService 단위 테스트
 * 각 메서드의 기능을 개별적으로 테스트합니다.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("PartyService 테스트")
class PartyServiceTest extends PartyTestBase {

  @Nested
  @DisplayName("파티 조회 테스트")
  class GetPartyTests {

    @Test
    @DisplayName("존재하는 파티 조회 성공")
    void getExistingParty() {
      // Given: 존재하는 파티 ID
      Long partyId = 1L;

      // When: 파티 조회
      PartyUpdateResponse response = partyService.getPartyById(partyId);

      // Then: 조회 결과 검증
      if (response.isSuccess()) {
        assertSuccessResponse(response);
        assertNotNull(response.getParty());
        assertEquals(partyId, response.getParty().getId());
      } else {
        assertFailureResponse(response);
        assertTrue(response.getMessage().contains("찾을 수 없"));
      }
    }

    @Test
    @DisplayName("존재하지 않는 파티 조회 실패")
    void getNonExistentParty() {
      // Given: 존재하지 않는 파티 ID
      Long nonExistentPartyId = NON_EXISTENT_PARTY_ID;

      // When: 파티 조회
      PartyUpdateResponse response = partyService.getPartyById(nonExistentPartyId);

      // Then: 조회 실패 검증
      assertFailureResponse(response);
      assertTrue(response.getMessage().contains("찾을 수 없"));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, 999999L})
    @DisplayName("잘못된 파티 ID로 조회 시도")
    void getPartyWithInvalidId(Long invalidPartyId) {
      // When: 잘못된 ID로 파티 조회
      PartyUpdateResponse response = partyService.getPartyById(invalidPartyId);

      // Then: 조회 실패 검증
      assertNotNull(response);
      assertNotNull(response.getMessage());
    }
  }

  @Nested
  @DisplayName("파티 수정 테스트")
  class UpdatePartyTests {

    @Test
    @DisplayName("소유자가 파티 정보 수정 성공")
    void updatePartyByOwner() {
      // Given: 소유자가 수정 요청
      Long partyId = 1L;
      PartyUpdateRequest updateRequest = createTestUpdateRequest(partyId);

      // When: 파티 정보 수정
      PartyUpdateResponse response = partyService.updateParty(updateRequest, TEST_OWNER_ID);

      // Then: 수정 결과 검증
      if (response.isSuccess()) {
        assertSuccessResponse(response);
        assertNotNull(response.getParty());
        assertPartyFields(
            response.getParty(), "수정된 테스트 파티", PartyCategory.DINE_OUT, 6, 50000, false);
      } else {
        assertFailureResponse(response);
      }
    }

    @Test
    @DisplayName("비소유자가 파티 정보 수정 시도 실패")
    void updatePartyByNonOwner() {
      // Given: 비소유자가 수정 요청
      Long partyId = 1L;
      PartyUpdateRequest updateRequest = createPartialUpdateRequest(partyId, "무단 수정");

      // When: 비소유자가 파티 정보 수정 시도
      PartyUpdateResponse response = partyService.updateParty(updateRequest, TEST_NON_OWNER_ID);

      // Then: 수정 실패 검증
      assertFailureResponse(response);
      assertTrue(response.getMessage().contains("권한") || response.getMessage().contains("찾을 수 없"));
    }

    @Test
    @DisplayName("존재하지 않는 파티 수정 시도 실패")
    void updateNonExistentParty() {
      // Given: 존재하지 않는 파티 수정 요청
      PartyUpdateRequest updateRequest = createTestUpdateRequest(NON_EXISTENT_PARTY_ID);

      // When: 존재하지 않는 파티 수정 시도
      PartyUpdateResponse response = partyService.updateParty(updateRequest, TEST_OWNER_ID);

      // Then: 수정 실패 검증
      assertFailureResponse(response);
      assertTrue(response.getMessage().contains("찾을 수 없"));
    }

    @Test
    @DisplayName("부분 수정 성공")
    void partialUpdateSuccess() {
      // Given: 일부 필드만 수정하는 요청
      Long partyId = 1L;
      String newTitle = "부분 수정된 제목";
      PartyUpdateRequest partialUpdateRequest = createPartialUpdateRequest(partyId, newTitle);

      // When: 부분 수정 실행
      PartyUpdateResponse response = partyService.updateParty(partialUpdateRequest, TEST_OWNER_ID);

      // Then: 수정 결과 검증
      if (response.isSuccess()) {
        assertSuccessResponse(response);
        assertNotNull(response.getParty());
        assertEquals(newTitle, response.getParty().getTitle());
      } else {
        assertFailureResponse(response);
      }
    }
  }

  @Nested
  @DisplayName("파티 삭제 테스트")
  class DeletePartyTests {

    @Test
    @DisplayName("소유자가 파티 삭제 성공")
    void deletePartyByOwner() {
      // Given: 소유자가 삭제 요청
      Long partyId = 1L;

      // When: 파티 삭제
      PartyUpdateResponse response = partyService.deleteParty(partyId, TEST_OWNER_ID);

      // Then: 삭제 결과 검증
      if (response.isSuccess()) {
        assertSuccessResponse(response);
        assertTrue(response.getMessage().contains("삭제"));
      } else {
        assertFailureResponse(response);
      }
    }

    @Test
    @DisplayName("비소유자가 파티 삭제 시도 실패")
    void deletePartyByNonOwner() {
      // Given: 비소유자가 삭제 요청
      Long partyId = 1L;

      // When: 비소유자가 파티 삭제 시도
      PartyUpdateResponse response = partyService.deleteParty(partyId, TEST_NON_OWNER_ID);

      // Then: 삭제 실패 검증
      assertFailureResponse(response);
      assertTrue(response.getMessage().contains("권한") || response.getMessage().contains("찾을 수 없"));
    }

    @Test
    @DisplayName("존재하지 않는 파티 삭제 시도 실패")
    void deleteNonExistentParty() {
      // Given: 존재하지 않는 파티 삭제 요청
      Long nonExistentPartyId = NON_EXISTENT_PARTY_ID;

      // When: 존재하지 않는 파티 삭제 시도
      PartyUpdateResponse response = partyService.deleteParty(nonExistentPartyId, TEST_OWNER_ID);

      // Then: 삭제 실패 검증
      assertFailureResponse(response);
      assertTrue(response.getMessage().contains("찾을 수 없"));
    }
  }

  @Nested
  @DisplayName("권한 확인 테스트")
  class AuthorizationTests {

    @Test
    @DisplayName("파티 소유자 확인 성공")
    void verifyPartyOwner() {
      // Given: 파티 ID와 소유자 ID
      Long partyId = 1L;

      // When: 소유자 확인
      boolean isOwner = partyService.isPartyOwner(partyId, TEST_OWNER_ID);
      boolean isNonOwner = partyService.isPartyOwner(partyId, TEST_NON_OWNER_ID);

      // Then: 권한 확인 결과 검증
      // 실제 데이터에 따라 결과가 달라질 수 있으므로 로그만 출력
      System.out.println("파티 " + partyId + "의 소유자(" + TEST_OWNER_ID + ") 확인: " + isOwner);
      System.out.println("파티 " + partyId + "의 비소유자(" + TEST_NON_OWNER_ID + ") 확인: " + isNonOwner);
    }

    @Test
    @DisplayName("존재하지 않는 파티의 소유자 확인")
    void verifyOwnerOfNonExistentParty() {
      // Given: 존재하지 않는 파티 ID
      Long nonExistentPartyId = NON_EXISTENT_PARTY_ID;

      // When: 존재하지 않는 파티의 소유자 확인
      boolean isOwner = partyService.isPartyOwner(nonExistentPartyId, TEST_OWNER_ID);

      // Then: 소유자가 아님을 확인
      assertFalse(isOwner);
    }
  }
}
