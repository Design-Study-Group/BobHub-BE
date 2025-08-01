package com.bobhub.party;

import com.bobhub.domain.Party;
import com.bobhub.domain.PartyCategory;
import com.bobhub.dto.PartyCreateRequest;
import com.bobhub.dto.PartyUpdateRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** 테스트 데이터 생성을 위한 빌더 클래스 다양한 테스트 시나리오에 맞는 데이터를 쉽게 생성할 수 있습니다. */
public class PartyTestDataBuilder {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  /** 기본 파티 생성 요청 빌더 */
  public static PartyCreateRequestBuilder createPartyRequest() {
    return new PartyCreateRequestBuilder();
  }

  /** 기본 파티 수정 요청 빌더 */
  public static PartyUpdateRequestBuilder createUpdateRequest() {
    return new PartyUpdateRequestBuilder();
  }

  /** 기본 파티 엔티티 빌더 */
  public static PartyEntityBuilder createParty() {
    return new PartyEntityBuilder();
  }

  /** 파티 생성 요청 빌더 */
  public static class PartyCreateRequestBuilder {
    private String category = "DELIVERY";
    private String title = "테스트 파티";
    private int limitPeople = 4;
    private int limitPrice = 30000;
    private Long ownerId = 61L;
    private boolean isOpen = true;
    private String finishedAt = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER);

    public PartyCreateRequestBuilder category(String category) {
      this.category = category;
      return this;
    }

    public PartyCreateRequestBuilder title(String title) {
      this.title = title;
      return this;
    }

    public PartyCreateRequestBuilder limitPeople(int limitPeople) {
      this.limitPeople = limitPeople;
      return this;
    }

    public PartyCreateRequestBuilder limitPrice(int limitPrice) {
      this.limitPrice = limitPrice;
      return this;
    }

    public PartyCreateRequestBuilder ownerId(Long ownerId) {
      this.ownerId = ownerId;
      return this;
    }

    public PartyCreateRequestBuilder isOpen(boolean isOpen) {
      this.isOpen = isOpen;
      return this;
    }

    public PartyCreateRequestBuilder finishedAt(String finishedAt) {
      this.finishedAt = finishedAt;
      return this;
    }

    public PartyCreateRequest build() {
      return PartyCreateRequest.builder()
          .category(category)
          .title(title)
          .limitPeople(limitPeople)
          .limitPrice(limitPrice)
          .ownerId(ownerId)
          .isOpen(isOpen)
          .finishedAt(finishedAt)
          .build();
    }
  }

  /** 파티 수정 요청 빌더 */
  public static class PartyUpdateRequestBuilder {
    private Long id = 1L;
    private String title = "수정된 테스트 파티";
    private PartyCategory category = PartyCategory.DINE_OUT;
    private Integer limitPeople = 6;
    private Integer limitPrice = 50000;
    private LocalDateTime finishedAt = LocalDateTime.now().plusDays(2);
    private Boolean isOpen = false;

    public PartyUpdateRequestBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public PartyUpdateRequestBuilder title(String title) {
      this.title = title;
      return this;
    }

    public PartyUpdateRequestBuilder category(PartyCategory category) {
      this.category = category;
      return this;
    }

    public PartyUpdateRequestBuilder limitPeople(Integer limitPeople) {
      this.limitPeople = limitPeople;
      return this;
    }

    public PartyUpdateRequestBuilder limitPrice(Integer limitPrice) {
      this.limitPrice = limitPrice;
      return this;
    }

    public PartyUpdateRequestBuilder finishedAt(LocalDateTime finishedAt) {
      this.finishedAt = finishedAt;
      return this;
    }

    public PartyUpdateRequestBuilder isOpen(Boolean isOpen) {
      this.isOpen = isOpen;
      return this;
    }

    public PartyUpdateRequest build() {
      return PartyUpdateRequest.builder()
          .id(id)
          .title(title)
          .category(category)
          .limitPeople(limitPeople)
          .limitPrice(limitPrice)
          .finishedAt(finishedAt)
          .isOpen(isOpen)
          .build();
    }
  }

  /** 파티 엔티티 빌더 */
  public static class PartyEntityBuilder {
    private long id = 1L;
    private PartyCategory category = PartyCategory.DELIVERY;
    private String title = "테스트 파티";
    private int limitPeople = 4;
    private int limitPrice = 30000;
    private long ownerId = 61L;
    private long memberId = 0L;
    private boolean isOpen = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime finishedAt = LocalDateTime.now().plusDays(1);

    public PartyEntityBuilder id(long id) {
      this.id = id;
      return this;
    }

    public PartyEntityBuilder category(PartyCategory category) {
      this.category = category;
      return this;
    }

    public PartyEntityBuilder title(String title) {
      this.title = title;
      return this;
    }

    public PartyEntityBuilder limitPeople(int limitPeople) {
      this.limitPeople = limitPeople;
      return this;
    }

    public PartyEntityBuilder limitPrice(int limitPrice) {
      this.limitPrice = limitPrice;
      return this;
    }

    public PartyEntityBuilder ownerId(long ownerId) {
      this.ownerId = ownerId;
      return this;
    }

    public PartyEntityBuilder memberId(long memberId) {
      this.memberId = memberId;
      return this;
    }

    public PartyEntityBuilder isOpen(boolean isOpen) {
      this.isOpen = isOpen;
      return this;
    }

    public PartyEntityBuilder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public PartyEntityBuilder finishedAt(LocalDateTime finishedAt) {
      this.finishedAt = finishedAt;
      return this;
    }

    public Party build() {
      return Party.builder()
          .id(id)
          .category(category)
          .title(title)
          .limitPeople(limitPeople)
          .limitPrice(limitPrice)
          .ownerId(ownerId)
          .memberId(memberId)
          .isOpen(isOpen)
          .createdAt(createdAt)
          .finishedAt(finishedAt)
          .build();
    }
  }

  /** 미리 정의된 테스트 데이터 생성 메서드들 */
  public static PartyCreateRequest deliveryParty() {
    return createPartyRequest()
        .category("DELIVERY")
        .title("배달 파티")
        .limitPeople(4)
        .limitPrice(40000)
        .build();
  }

  public static PartyCreateRequest dineOutParty() {
    return createPartyRequest()
        .category("DINE_OUT")
        .title("외식 파티")
        .limitPeople(6)
        .limitPrice(50000)
        .build();
  }

  public static PartyCreateRequest lunchboxParty() {
    return createPartyRequest()
        .category("LUNCHBOX")
        .title("도시락 파티")
        .limitPeople(8)
        .limitPrice(15000)
        .build();
  }

  public static PartyUpdateRequest partialUpdateRequest(Long partyId, String newTitle) {
    return createUpdateRequest().id(partyId).title(newTitle).build();
  }

  public static PartyUpdateRequest fullUpdateRequest(Long partyId) {
    return createUpdateRequest()
        .id(partyId)
        .title("완전 수정된 파티")
        .category(PartyCategory.LUNCHBOX)
        .limitPeople(10)
        .limitPrice(20000)
        .isOpen(false)
        .build();
  }
}
