# BobHub

## 환경 설정

### 프로필별 설정

이 프로젝트는 환경별로 설정을 분리하여 관리합니다:

- **개발 환경**: `dev` 프로필 사용
- **프로덕션 환경**: `prod` 프로필 사용
- **테스트 환경**: `test` 프로필 사용

### 환경별 실행 방법

```bash
# 개발 환경 실행
./gradlew bootRun --args='--spring.profiles.active=dev'

# 프로덕션 환경 실행
./gradlew bootRun --args='--spring.profiles.active=prod'

# 테스트 실행
./gradlew test
```

### 환경 변수 설정

#### 개발 환경 (application-dev.yml)
- **데이터베이스**: `jdbc:mysql://localhost:3306/bobhub_dev`
- **OAuth 리다이렉트**: `http://localhost:8080/login/oauth2/code/google`
- **로깅 레벨**: DEBUG

#### 프로덕션 환경 (application-prod.yml)
- **데이터베이스**: 환경 변수로 설정
- **OAuth 리다이렉트**: `https://developlee20.store/login/oauth2/code/google`
- **로깅 레벨**: INFO

### 필요한 환경 변수

```bash
# 데이터베이스 설정
SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/your-db-name
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
SPRING_DATASOURCE_DRIVER=com.mysql.cj.jdbc.Driver

# Google OAuth 설정
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
```

---

## 키워드 (작업 내용 분류)

| 태그      | 설명                                                       |
|-----------|------------------------------------------------------------|
| Feat      | 새로운 기능 추가                                           |
| Add       | 파일 추가                                                  |
| Del       | 파일 삭제                                                  |
| Fix       | 버그 수정                                                  |
| Docs      | 문서 수정                                                  |
| Style     | 스타일 변경, 세미콜론 누락 등 **코드 변경이 없는 경우**   |
| Test      | 테스트 코드 추가, 리팩토링된 테스트 코드                   |
| Chore     | 빌드 설정, 패키지 수정, 기타 작업                          |
| Refactor  | 코드 리팩토링 (기능 변화 없이 구조 개선)                   |
| CI        | CI 설정 (예: GitHub Actions) 관련 작업                     |

---

## 브랜치 명명 규칙

[키워드]-[issue_number]

예시:  
이슈 번호 5번에 대한 새로운 기능 개발 → `Feat-5`

---

## 커밋 메시지 규칙

- 형식:  
  [키워드] 작업 내용
- 예시:  
  Feat 회원가입 기능 추가

### 작성 규칙

- 키워드 첫 글자는 **대문자**
- 키워드와 내용 사이는 **한 칸 띄어쓰기**
- **제목은 간결하게**

## pre-commit 명령어

```bash
# poetry 설치가 안되어 있을 경우
pip install poetry

# poetry 종속성 동기화(최초 1회만 실행)
poetry install --no-root

# commit 이전에 검사 후 커밋
poetry run pre-commit run --all-files
```

---
