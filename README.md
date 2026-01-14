# OGIMS (온세상) - 자원봉사 매칭 플랫폼

> "이웃의 손길이 닿는 순간, 세상은 더 따뜻해집니다"

취약계층과 자원봉사자를 연결하는 웹 기반 봉사활동 매칭 서비스입니다.

---

## 프로젝트 개요

| 항목 | 내용 |
|------|------|
| **프로젝트명** | OGIMS (온세상 자원봉사 매칭 시스템) |
| **개발 기간** | 2024.08 |
| **개발 인원** | 팀 프로젝트 |
| **담당 역할** | Backend 개발, DB 설계 |

---

## 기술 스택

### Backend
| 기술 | 설명 |
|------|------|
| **Java 11** | 핵심 백엔드 언어 |
| **Servlet 4.0** | HTTP 요청/응답 처리 |
| **JSP/JSTL** | 동적 웹 페이지 렌더링 |
| **JDBC** | 데이터베이스 연동 |
| **Quartz Scheduler** | 배치 작업 스케줄링 (봉사 상태 자동 갱신) |

### Frontend
| 기술 | 설명 |
|------|------|
| **HTML5/CSS3** | 마크업 및 스타일링 |
| **JavaScript (ES6)** | 클라이언트 동적 기능 |
| **AJAX** | 비동기 댓글 처리 |

### Database
| 기술 | 설명 |
|------|------|
| **Oracle XE 11g** | 관계형 데이터베이스 |
| **PL/SQL** | 시퀀스, 제약조건 관리 |

### Server & Tools
| 기술 | 설명 |
|------|------|
| **Apache Tomcat 9.0** | WAS (Web Application Server) |
| **Eclipse IDE** | 개발 환경 |
| **Git** | 버전 관리 |

---

## 아키텍처

### MVC 패턴 + Front Controller
```
┌─────────────────────────────────────────────────────────────┐
│                        Client (Browser)                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    RequestFilter (UTF-8)                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│              ActionServlet (Front Controller)                │
│                    - URL → Action 매핑                       │
│                    - Forward / Redirect 처리                 │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Action (Controller)                     │
│   LoginAction, ListVolOfferAction, AddNoticeAction ...      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     Service (Business Logic)                 │
│     UserService, VolOfferService, NoticeService ...         │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        DAO (Data Access)                     │
│         UserDao, VolOfferDao, NoticeDao, CommentDao         │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     Oracle Database                          │
│            USERS, VOLUNTEER, NOTICE, COMMENTS               │
└─────────────────────────────────────────────────────────────┘
```

---

## 프로젝트 구조

```
semiProject/
├── src/main/java/com/semi/
│   ├── domain/                    # 도메인 모델 (Entity)
│   │   ├── User.java              # 사용자
│   │   ├── Post.java              # 게시글 추상 클래스
│   │   ├── VolOffer.java          # 봉사 제공 (extends Post)
│   │   ├── VolRequest.java        # 봉사 요청 (extends Post)
│   │   ├── Notice.java            # 공지사항
│   │   └── Comment.java           # 댓글
│   │
│   ├── framework/                 # MVC 프레임워크
│   │   ├── ActionServlet.java     # Front Controller
│   │   ├── Action.java            # Action 추상 클래스
│   │   └── RequestMapping.java    # URL-Action 매핑
│   │
│   ├── view/                      # Controller (Action)
│   │   ├── user/                  # 회원 관련 Action
│   │   ├── volOffer/              # 봉사 제공 Action
│   │   ├── volRequest/            # 봉사 요청 Action
│   │   ├── notice/                # 공지사항 Action
│   │   └── comment/               # 댓글 Action
│   │
│   ├── service/                   # Business Logic
│   │   ├── user/
│   │   ├── volOffer/
│   │   ├── volRequest/
│   │   ├── notice/
│   │   └── comment/
│   │
│   ├── common/                    # 공통 유틸리티
│   │   ├── util/
│   │   │   ├── DBUtil.java        # DB 커넥션 관리
│   │   │   ├── PasswordUtilSHA256.java  # 비밀번호 암호화
│   │   │   ├── HttpUtil.java      # Forward/Redirect 유틸
│   │   │   └── FileUtil.java      # 파일 업로드
│   │   └── filter/
│   │       └── RequestFilter.java # 인코딩 필터
│   │
│   └── batch/                     # 배치 작업
│       ├── QuartzBootListener.java
│       └── VolRequestStatusJob.java
│
├── src/main/webapp/
│   ├── home/                      # 메인 홈페이지
│   ├── user/                      # 회원 관련 JSP
│   ├── volOffer/                  # 봉사 제공 JSP
│   ├── volRequest/                # 봉사 요청 JSP
│   ├── notice/                    # 공지사항 JSP
│   ├── comment/                   # 댓글 JSP Fragment
│   ├── common/                    # 공통 레이아웃
│   ├── css/                       # 스타일시트
│   ├── js/                        # JavaScript
│   ├── images/                    # 이미지 리소스
│   └── WEB-INF/
│       ├── web.xml                # 서블릿 설정
│       └── lib/                   # 라이브러리 (JAR)
│
└── README.md
```

---

## 주요 기능

### 1. 회원 관리
- 회원가입 / 로그인 / 로그아웃
- 비밀번호 SHA-256 암호화
- 회원정보 수정 / 비밀번호 변경
- 회원 탈퇴

### 2. 봉사 제공 (VolOffer)
- 봉사활동 등록 (제목, 내용, 일정, 지역, 카테고리)
- 이미지 첨부 기능
- 목록 조회 (페이징, 검색)
- 상세 조회 / 수정 / 삭제
- 봉사 상태 관리 (모집중/마감)

### 3. 봉사 요청 (VolRequest)
- 도움 요청 게시글 작성
- 지역/카테고리별 필터링
- 매칭 상태 관리

### 4. 댓글 시스템
- AJAX 기반 실시간 댓글 CRUD
- 비동기 처리로 페이지 새로고침 없이 동작

### 5. 공지사항
- 관리자(admin) 전용 등록/수정/삭제
- 일반 사용자 조회

### 6. 배치 스케줄러
- Quartz를 활용한 봉사 상태 자동 갱신
- 마감 시간 경과 시 자동 상태 변경

---

## 데이터베이스 설계

### ERD
```
┌──────────────┐       ┌──────────────┐
│    USERS     │       │    NOTICE    │
├──────────────┤       ├──────────────┤
│ USERID (PK)  │◄──────│ AUTHORID(FK) │
│ PASSWORD     │       │ NOTICEID(PK) │
│ NAME         │       │ TITLE        │
│ REGION       │       │ CONTENT      │
│ PHONE        │       │ CREATEDAT    │
│ GENDER       │       └──────────────┘
│ BIRTHDATE    │
│ CATEGORY     │       ┌──────────────┐
│ STATUS       │       │  VOLUNTEER   │
└──────────────┘       ├──────────────┤
       │               │VOLUNTEERID(PK)│
       │               │ AUTHORID(FK) │◄───┐
       │               │ TITLE        │    │
       └──────────────►│ CONTENT      │    │
                       │ PHONE        │    │
                       │ REGION       │    │
                       │ CATEGORY     │    │
                       │ STARTTIME    │    │
                       │ ENDTIME      │    │
                       │ STATUS       │    │
                       │ FLAG         │    │
                       │ IMAGE        │    │
                       │ CREATEDAT    │    │
                       └──────────────┘    │
                              │            │
                              ▼            │
                       ┌──────────────┐    │
                       │   COMMENTS   │    │
                       ├──────────────┤    │
                       │COMMENTID(PK) │    │
                       │ AUTHORID(FK) │────┘
                       │VOLUNTEERID(FK)│
                       │ CONTENT      │
                       │ CREATEDAT    │
                       └──────────────┘
```

### 테이블 명세

| 테이블명 | 설명 | 주요 컬럼 |
|----------|------|-----------|
| USERS | 회원 정보 | USERID, PASSWORD(SHA-256), NAME, REGION |
| VOLUNTEER | 봉사 게시글 | VOLUNTEERID, TITLE, CONTENT, STARTTIME, STATUS |
| NOTICE | 공지사항 | NOTICEID, TITLE, CONTENT, CREATEDAT |
| COMMENTS | 댓글 | COMMENTID, CONTENT, VOLUNTEERID(FK) |

---

## 보안 처리

| 항목 | 적용 기술 |
|------|-----------|
| 비밀번호 암호화 | SHA-256 해시 |
| SQL Injection 방지 | PreparedStatement 사용 |
| XSS 방지 | JSTL `fn:escapeXml()` 적용 |
| 인코딩 필터 | UTF-8 강제 적용 |
| 세션 관리 | Cookie 기반 세션 트래킹 |

---

## 실행 방법

### 1. 환경 설정
```
- JDK 11 이상
- Apache Tomcat 9.0
- Oracle XE 11g
```

### 2. 데이터베이스 설정
```sql
-- Oracle 사용자 생성
CREATE USER ogims IDENTIFIED BY ogims;
GRANT CONNECT, RESOURCE TO ogims;

-- 테이블 생성 스크립트 실행
@ogims_db_backup.sql
```

### 3. DB 연결 정보 (DBUtil.java)
```java
url = "jdbc:oracle:thin:@localhost:1521:xe"
user = "ogims"
password = "ogims"
```

### 4. 서버 실행
```
1. Eclipse에서 프로젝트 Import
2. Tomcat 서버에 프로젝트 추가
3. 서버 시작
4. http://localhost:8000/semiProject 접속
```

---

## 화면 구성

| 페이지 | 설명 |
|--------|------|
| `/home/home.jsp` | 메인 홈페이지 (이미지 슬라이더) |
| `/user/loginView.jsp` | 로그인 페이지 |
| `/user/addUserView.jsp` | 회원가입 페이지 |
| `/volOffer/listVolOffer.jsp` | 봉사 제공 목록 |
| `/volRequest/listVolRequest.jsp` | 봉사 요청 목록 |
| `/notice/listNoticeView.jsp` | 공지사항 목록 |

---

## 트러블 슈팅

### 1. LocalDateTime과 JSTL 호환 문제
- **문제**: `fmt:formatDate`가 Java 8 `LocalDateTime`을 지원하지 않음
- **해결**: Domain 클래스에 `getCreatedAtString()` 메서드 추가하여 EL에서 직접 접근

### 2. Front Controller Forward/Redirect 처리
- **문제**: ActionServlet에서 Forward와 Redirect 구분 로직 오류
- **해결**: `redirect:` prefix 명시적 구분, Context Path 자동 추가 처리

### 3. Oracle DATE 타입 변환
- **문제**: Oracle DATE → Java LocalDateTime 변환 시 데이터 손실
- **해결**: `ResultSet.getTimestamp()` 사용 후 `toLocalDateTime()` 변환

---

## 개선 사항 (향후 계획)

- [ ] Spring Framework로 마이그레이션
- [ ] REST API 구조 도입
- [ ] JWT 기반 인증 시스템
- [ ] 반응형 웹 디자인 강화
- [ ] 실시간 알림 기능 (WebSocket)

---

## 라이선스

This project is licensed under the MIT License.

---

## 연락처

- Email: your-email@example.com
- GitHub: https://github.com/your-username
