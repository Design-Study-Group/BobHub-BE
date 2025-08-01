# 파티 테스트 코드 구조

## 개요
파티 상세정보 조회, 수정, 삭제 기능을 위한 고도화된 테스트 코드 구조입니다.

## 테스트 구조

### 1. PartyTestBase.java
- 모든 테스트 클래스의 베이스 클래스
- 공통 테스트 데이터와 헬퍼 메서드 제공
- `@BeforeEach`로 테스트 전 공통 설정
- 테스트용 상수와 유틸리티 메서드 포함

### 2. PartyServiceTest.java
- PartyService의 단위 테스트
- `@Nested` 클래스를 사용한 구조화된 테스트
- 각 기능별로 분리된 테스트 그룹
- `@ParameterizedTest`를 사용한 다양한 입력값 테스트

### 3. PartyIntegrationTest.java
- 통합 테스트 (여러 기능이 함께 작동하는 시나리오)
- `@TestMethodOrder`를 사용한 테스트 실행 순서 제어
- 생명주기, 권한, 예외 상황, 데이터 일관성 테스트

### 4. PartyTestDataBuilder.java
- 테스트 데이터 생성을 위한 빌더 패턴
- 플루언트 API를 사용한 직관적인 테스트 데이터 생성
- 미리 정의된 테스트 시나리오 제공

## 테스트 실행 방법

### 1. 전체 테스트 실행
```bash
./gradlew test
```

### 2. 특정 테스트 클래스 실행
```bash
./gradlew test --tests PartyServiceTest
./gradlew test --tests PartyIntegrationTest
```

### 3. 특정 테스트 메서드 실행
```bash
./gradlew test --tests PartyServiceTest.GetPartyTests.getExistingParty
```

### 4. IDE에서 실행
- 각 테스트 메서드 옆의 실행 버튼 클릭
- 클래스 레벨에서 전체 테스트 실행

## 테스트 패턴

### 1. Given-When-Then 패턴
```java
@Test
void testMethod() {
    // Given: 테스트 준비
    Long partyId = 1L;
    
    // When: 테스트 실행
    PartyUpdateResponse response = partyService.getPartyById(partyId);
    
    // Then: 결과 검증
    assertTrue(response.isSuccess());
}
```

### 2. Nested 클래스 구조
```java
@Nested
@DisplayName("파티 조회 테스트")
class GetPartyTests {
    // 관련된 테스트들을 그룹화
}
```

### 3. Parameterized 테스트
```java
@ParameterizedTest
@ValueSource(longs = {0L, -1L, 999999L})
void testWithInvalidIds(Long invalidId) {
    // 다양한 입력값으로 테스트
}
```

### 4. 빌더 패턴
```java
PartyCreateRequest request = PartyTestDataBuilder
    .createPartyRequest()
    .title("테스트 파티")
    .category("DELIVERY")
    .limitPeople(4)
    .build();
```

## 테스트 환경 설정

### 1. application-test.yml
- H2 인메모리 데이터베이스 사용
- 테스트 전용 설정
- SQL 로깅 활성화

### 2. @ActiveProfiles("test")
- 테스트 프로파일 활성화
- 테스트 전용 설정 파일 사용

### 3. @Transactional
- 테스트 격리 보장
- 각 테스트 후 롤백

## 테스트 커버리지

### 1. 단위 테스트
- 개별 메서드 기능 테스트
- 다양한 입력값과 예외 상황 테스트
- 권한 검증 테스트

### 2. 통합 테스트
- 전체 워크플로우 테스트
- 데이터 일관성 테스트
- 권한 기반 통합 테스트

### 3. 예외 처리 테스트
- 존재하지 않는 데이터 테스트
- 잘못된 입력값 테스트
- 권한 없는 사용자 테스트

## 테스트 데이터 관리

### 1. 상수 정의
```java
protected static final Long TEST_OWNER_ID = 61L;
protected static final Long TEST_NON_OWNER_ID = 999L;
protected static final Long NON_EXISTENT_PARTY_ID = 99999L;
```

### 2. 헬퍼 메서드
```java
protected PartyCreateRequest createTestPartyRequest() {
    // 테스트용 파티 생성 요청
}

protected void assertSuccessResponse(PartyUpdateResponse response) {
    // 성공 응답 검증
}
```

### 3. 빌더 패턴
```java
PartyCreateRequest request = PartyTestDataBuilder
    .deliveryParty()  // 미리 정의된 시나리오
    .title("커스텀 제목")
    .build();
```

## 모범 사례

### 1. 테스트 명명
- `@DisplayName`을 사용한 명확한 테스트 설명
- 메서드명은 동작을 명확히 표현
- 한글로 테스트 목적 명시

### 2. 테스트 격리
- 각 테스트는 독립적으로 실행 가능
- `@Transactional`로 데이터 격리
- 공통 설정은 `@BeforeEach`에서 처리

### 3. 검증
- 성공/실패 케이스 모두 테스트
- 예외 상황 적절히 처리
- 데이터 일관성 검증

### 4. 유지보수성
- 중복 코드 제거
- 헬퍼 메서드 활용
- 빌더 패턴으로 테스트 데이터 생성

## 실행 결과 확인

### 1. 콘솔 출력
- 테스트 실행 과정 로그 확인
- SQL 쿼리 로그 확인
- 에러 메시지 확인

### 2. 테스트 리포트
- 성공/실패 테스트 개수
- 실행 시간
- 커버리지 정보

### 3. 디버깅
- IDE 디버거 사용
- 중단점 설정
- 변수 값 확인 