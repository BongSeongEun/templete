# Spring Boot Template

> JWT 인증 + MyBatis + MariaDB + Redis 기반의 Spring Boot 백엔드 템플릿

새 프로젝트를 위해 만든 템플릿입니다.
인증/인가, 예외 처리, 공통 응답 포맷이 세팅되어 있어 도메인 로직 개발에만 집중할 수 있습니다.

---

## 기술 스택

| 분류 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0 |
| ORM | MyBatis |
| DB | MariaDB |
| Cache / Session | Redis |
| 인증 | JWT (jjwt 0.12.6) |
| 문서 | Springdoc OpenAPI (Swagger UI) |
| 기타 | Lombok |

---

## 프로젝트 구조

```
src/main/java/com/example/templete/
├── domain/
│   ├── user/                   # 인증 도메인 (회원가입, 로그인, 토큰 갱신)
│   │   ├── controller/
│   │   ├── service/
│   │   ├── mapper/
│   │   └── model/
│   └── todo/                   # 예시 도메인
│       ├── controller/
│       ├── service/
│       ├── mapper/
│       └── model/
└── global/
    ├── common/                 # 공통 응답 포맷 (ApiResponse)
    ├── config/                 # Security 설정
    ├── error/                  # 전역 예외 처리 (GlobalExceptionHandler, ErrorCode)
    └── security/               # JWT 필터, JWT 발급/검증
```

---

## 시작하기

### 사전 준비

- Java 17
- MariaDB
- Redis

### 1. DB 테이블 생성

`src/main/resources/sql/` 폴더의 SQL 파일을 순서대로 실행합니다.

```sql
-- user 테이블
create table user (
    user_id        int auto_increment primary key,
    user_uuid      char(36) default (uuid()) not null unique,
    user_name      varchar(20)               not null,
    login_id       varchar(50)               not null unique,
    login_password varchar(255)              not null,
    role           varchar(20) default 'ROLE_USER' not null
);

-- todo 테이블
create table todo (
    todo_id         int auto_increment primary key,
    todo_name       varchar(255)         not null,
    todo_start_time timestamp            not null,
    todo_end_time   timestamp            not null,
    todo_status     tinyint(1) default 0 not null
);
```

### 2. application.properties 설정

```properties
# DB
spring.datasource.url=jdbc:mariadb://localhost:3306/{DB명}?characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username={유저명}
spring.datasource.password={비밀번호}

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT
em.jwt.secret={32자 이상의 시크릿 키}
em.jwt.expireTime=600000          # 액세스 토큰 만료 (ms) - 10분
em.jwt.refreshExpireTime=864000000 # 리프레시 토큰 만료 (ms) - 10일
```

### 3. 실행

```bash
./gradlew bootRun
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

---

## API

### 인증 (`/api/v1/auth`)

| Method | URL | 설명 | 인증 필요 |
|---|---|---|---|
| POST | `/api/v1/auth/signUp` | 회원가입 | X |
| POST | `/api/v1/auth/login` | 로그인 | X |
| POST | `/api/v1/auth/renewToken` | 토큰 갱신 | X |

### 회원가입

```http
POST /api/v1/auth/signUp
Content-Type: application/json

{
  "userName": "홍길동",
  "loginId": "gildong",
  "loginPassword": "password123"
}
```

### 로그인

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "loginId": "gildong",
  "loginPassword": "password123"
}
```

응답 시 `accessToken`, `refreshToken`이 **httpOnly 쿠키**로 자동 설정됩니다.

### 토큰 갱신

```http
POST /api/v1/auth/renewToken
```

쿠키의 `refreshToken`을 Redis와 대조하여 검증합니다.
성공 시 새 토큰 쌍을 쿠키로 응답하고 기존 리프레시 토큰은 즉시 삭제됩니다 (토큰 로테이션).

---

## 인증 흐름

```
로그인
  └─ accessToken (쿠키, 10분) + refreshToken (쿠키, 10일) 발급
  └─ refreshToken → Redis 저장 (TTL 10일, 자동 만료)

인증이 필요한 API 요청
  └─ JwtFilter가 쿠키의 accessToken 검증
  └─ 유효하면 SecurityContext에 인증 정보 설정

accessToken 만료 시
  └─ POST /api/v1/auth/renewToken 호출
  └─ refreshToken을 Redis와 대조
  └─ 일치하면 새 토큰 쌍 발급 + 기존 refreshToken 삭제
```

---

## 공통 응답 포맷

```json
// 성공 (데이터 있음)
{
  "code": "SUCCESS",
  "message": "요청이 성공적으로 완료되었습니다.",
  "data": { ... }
}

// 성공 (데이터 없음)
{
  "code": "SUCCESS",
  "message": "요청이 성공적으로 완료되었습니다."
}

// 실패
{
  "code": "USER-0001",
  "message": "해당하는 id의 user가 존재하지 않습니다."
}
```

---

## 새 도메인 추가하기

`todo` 도메인을 참고하여 아래 순서로 추가합니다.

1. `src/main/resources/sql/` 에 테이블 DDL 추가
2. `domain/{도메인명}/` 하위에 `controller`, `service`, `mapper`, `model` 패키지 생성
3. `src/main/resources/mapper/{도메인명}/` 에 MyBatis XML Mapper 추가
4. 인증이 필요 없는 경로는 `SecurityConfig`의 `permitAll()`에 추가

---

## 환경변수 분리 (운영 배포 시)

`application.properties`에 하드코딩된 값들은 운영 환경에서 반드시 환경변수로 분리하세요.

```bash
export SPRING_DATASOURCE_PASSWORD=your_password
export EM_JWT_SECRET=your_secret_key
```
