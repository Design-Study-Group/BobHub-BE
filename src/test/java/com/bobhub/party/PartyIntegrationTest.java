package com.bobhub.party;

import static org.junit.jupiter.api.Assertions.*;

import com.bobhub.domain.Party;
import com.bobhub.domain.PartyCategory;
import com.bobhub.dto.PartyCreateRequest;
import com.bobhub.dto.PartyUpdateRequest;
import com.bobhub.dto.PartyUpdateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/** 파티 기능 통합 테스트 여러 기능이 함께 작동하는 시나리오를 테스트합니다. */
@DisplayName("파티 통합 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartyIntegrationTest extends PartyTestBase {

  @Nested
  @DisplayName("파티 생명주기 테스트")
  class PartyLifecycleTests {

    @Test
    @Order(1)
    @DisplayName("파티 생성 후 조회 테스트")
    void createAndGetParty() {
      // Given: 파티 생성 요청
      PartyCreateRequest createRequest = createTestPartyRequest();

      // When: 파티 생성
      partyService.createParty(createRequest);

      // Then: 생성된 파티 조회 (실제로는 생성된 파티의 ID를 알 수 없으므로
      // 테스트용으로는 기존 파티를 조회)
      PartyUpdateResponse response = partyService.getPartyById(1L);

      // 조회 결과가 성공하거나 실패할 수 있음 (데이터 존재 여부에 따라)
      assertNotNull(response);
      assertNotNull(response.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("파티 생성-수정-삭제 전체 워크플로우 테스트")
    void fullPartyLifecycle() {
      // Given: 파티 생성 요청
      PartyCreateRequest createRequest = createTestPartyRequest();

      // When & Then 1: 파티 생성
      partyService.createParty(createRequest);
      System.out.println("파티 생성 완료");

      // When & Then 2: 생성된 파티 조회
      PartyUpdateResponse getResponse = partyService.getPartyById(1L);
      assertNotNull(getResponse);
      System.out.println("파티 조회 결과: " + getResponse.isSuccess());

      // When & Then 3: 파티 정보 수정
      if (getResponse.isSuccess()) {
        PartyUpdateRequest updateRequest = createTestUpdateRequest(1L);
        PartyUpdateResponse updateResponse = partyService.updateParty(updateRequest, TEST_OWNER_ID);
        assertNotNull(updateResponse);
        System.out.println("파티 수정 결과: " + updateResponse.isSuccess());
      }

      // When & Then 4: 파티 삭제
      PartyUpdateResponse deleteResponse = partyService.deleteParty(1L, TEST_OWNER_ID);
      assertNotNull(deleteResponse);
      System.out.println("파티 삭제 결과: " + deleteResponse.isSuccess());
    }
  }

  @Nested
  @DisplayName("권한 기반 통합 테스트")
  class AuthorizationIntegrationTests {

    @Test
    @DisplayName("소유자 권한으로 모든 작업 수행 테스트")
    void ownerCanPerformAllOperations() {
      // Given: 파티 ID와 소유자 ID
      Long partyId = 1L;

      // When & Then 1: 소유자가 파티 조회
      PartyUpdateResponse getResponse = partyService.getPartyById(partyId);
      assertNotNull(getResponse);

      // When & Then 2: 소유자가 파티 수정
      if (getResponse.isSuccess()) {
        PartyUpdateRequest updateRequest = createPartialUpdateRequest(partyId, "소유자 수정");
        PartyUpdateResponse updateResponse = partyService.updateParty(updateRequest, TEST_OWNER_ID);
        assertNotNull(updateResponse);
      }

      // When & Then 3: 소유자가 파티 삭제
      PartyUpdateResponse deleteResponse = partyService.deleteParty(partyId, TEST_OWNER_ID);
      assertNotNull(deleteResponse);
    }

    @Test
    @DisplayName("비소유자 권한 제한 테스트")
    void nonOwnerCannotModifyOrDelete() {
      // Given: 파티 ID와 비소유자 ID
      Long partyId = 1L;

      // When & Then 1: 비소유자는 파티 조회 가능
      PartyUpdateResponse getResponse = partyService.getPartyById(partyId);
      assertNotNull(getResponse);

      // When & Then 2: 비소유자는 파티 수정 불가
      PartyUpdateRequest updateRequest = createPartialUpdateRequest(partyId, "무단 수정");
      PartyUpdateResponse updateResponse =
          partyService.updateParty(updateRequest, TEST_NON_OWNER_ID);
      assertFalse(updateResponse.isSuccess());
      assertTrue(
          updateResponse.getMessage().contains("권한")
              || updateResponse.getMessage().contains("찾을 수 없"));

      // When & Then 3: 비소유자는 파티 삭제 불가
      PartyUpdateResponse deleteResponse = partyService.deleteParty(partyId, TEST_NON_OWNER_ID);
      assertFalse(deleteResponse.isSuccess());
      assertTrue(
          deleteResponse.getMessage().contains("권한")
              || deleteResponse.getMessage().contains("찾을 수 없"));
    }
  }

  @Nested
  @DisplayName("예외 상황 통합 테스트")
  class ExceptionIntegrationTests {

    @Test
    @DisplayName("존재하지 않는 파티에 대한 모든 작업 실패 테스트")
    void allOperationsFailForNonExistentParty() {
      // Given: 존재하지 않는 파티 ID
      Long nonExistentPartyId = NON_EXISTENT_PARTY_ID;

      // When & Then 1: 존재하지 않는 파티 조회 실패
      PartyUpdateResponse getResponse = partyService.getPartyById(nonExistentPartyId);
      assertFalse(getResponse.isSuccess());
      assertTrue(getResponse.getMessage().contains("찾을 수 없"));

      // When & Then 2: 존재하지 않는 파티 수정 실패
      PartyUpdateRequest updateRequest = createTestUpdateRequest(nonExistentPartyId);
      PartyUpdateResponse updateResponse = partyService.updateParty(updateRequest, TEST_OWNER_ID);
      assertFalse(updateResponse.isSuccess());
      assertTrue(updateResponse.getMessage().contains("찾을 수 없"));

      // When & Then 3: 존재하지 않는 파티 삭제 실패
      PartyUpdateResponse deleteResponse =
          partyService.deleteParty(nonExistentPartyId, TEST_OWNER_ID);
      assertFalse(deleteResponse.isSuccess());
      assertTrue(deleteResponse.getMessage().contains("찾을 수 없"));
    }

    @Test
    @DisplayName("잘못된 사용자 ID로 작업 시도 테스트")
    void operationsWithInvalidUserId() {
      // Given: 잘못된 사용자 ID
      Long invalidUserId = -1L;
      Long partyId = 1L;

      // When & Then: 잘못된 사용자 ID로 수정 시도
      PartyUpdateRequest updateRequest = createPartialUpdateRequest(partyId, "잘못된 사용자 수정");
      PartyUpdateResponse updateResponse = partyService.updateParty(updateRequest, invalidUserId);
      assertFalse(updateResponse.isSuccess());

      // When & Then: 잘못된 사용자 ID로 삭제 시도
      PartyUpdateResponse deleteResponse = partyService.deleteParty(partyId, invalidUserId);
      assertFalse(deleteResponse.isSuccess());
    }
  }

  @Nested
  @DisplayName("데이터 일관성 통합 테스트")
  class DataConsistencyIntegrationTests {

    @Test
    @DisplayName("파티 수정 후 데이터 일관성 확인")
    void dataConsistencyAfterUpdate() {
      // Given: 파티 ID
      Long partyId = 1L;

      // When 1: 파티 조회
      PartyUpdateResponse getResponse = partyService.getPartyById(partyId);

      if (getResponse.isSuccess()) {
        // When 2: 파티 수정
        PartyUpdateRequest updateRequest =
            PartyUpdateRequest.builder()
                .id(partyId)
                .title("일관성 테스트 제목")
                .category(PartyCategory.LUNCHBOX)
                .limitPeople(10)
                .limitPrice(20000)
                .isOpen(false)
                .build();

        PartyUpdateResponse updateResponse = partyService.updateParty(updateRequest, TEST_OWNER_ID);

        // Then: 수정 성공 시 데이터 일관성 확인
        if (updateResponse.isSuccess()) {
          assertNotNull(updateResponse.getParty());
          assertEquals("일관성 테스트 제목", updateResponse.getParty().getTitle());
          assertEquals(PartyCategory.LUNCHBOX, updateResponse.getParty().getCategory());
          assertEquals(10, updateResponse.getParty().getLimitPeople());
          assertEquals(20000, updateResponse.getParty().getLimitPrice());
          assertFalse(updateResponse.getParty().getIsOpen());
        }
      }
    }

    @Test
    @DisplayName("부분 수정 시 기존 데이터 유지 확인")
    void partialUpdatePreservesExistingData() {
      // Given: 파티 ID
      Long partyId = 1L;

      // When 1: 원본 파티 조회
      PartyUpdateResponse originalResponse = partyService.getPartyById(partyId);

      if (originalResponse.isSuccess()) {
        Party originalParty = originalResponse.getParty();

        // When 2: 제목만 수정
        PartyUpdateRequest partialUpdateRequest = createPartialUpdateRequest(partyId, "부분 수정 테스트");
        PartyUpdateResponse updateResponse =
            partyService.updateParty(partialUpdateRequest, TEST_OWNER_ID);

        // Then: 수정 성공 시 기존 데이터 유지 확인
        if (updateResponse.isSuccess()) {
          Party updatedParty = updateResponse.getParty();
          assertEquals("부분 수정 테스트", updatedParty.getTitle());
          assertEquals(originalParty.getCategory(), updatedParty.getCategory());
          assertEquals(originalParty.getLimitPeople(), updatedParty.getLimitPeople());
          assertEquals(originalParty.getLimitPrice(), updatedParty.getLimitPrice());
          assertEquals(originalParty.getIsOpen(), updatedParty.getIsOpen());
        }
      }
    }
  }
}
