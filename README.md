# 💫 ZARIT

## 📝 프로젝트 소개
**개발 목표**  
신촌에서 공부하기 좋은 카페를 지도에서 확인하고, 공부하면 제휴 공간에서 쓸 수 있는 리워드를 받는 서비스

**주요 기능**
- 사용자 로그인 및 선호도 설정
- AI를 활용한 혼잡도 예측 및 장소 추천
- 위치 기반 장소(Pin) 정보 조회
- 퀘스트 시스템을 통한 장소 정보 업데이트
- 사진 업로드 및 조회 기능

---

## 🚀 기술 스택
- **언어**: Java 17  
- **프레임워크**: Spring Boot  
- **데이터베이스**: MySQL  
- **의존성 관리**: Gradle  
- **데이터 접근**: Spring Data JPA  
- **유틸리티**: Lombok, Spring Boot Starter Validation  

---

## 🧑‍💻 프로젝트 실행 방법
로컬 환경에서 프로젝트를 실행하기 위한 단계별 방법입니다.

### 1. 레포지토리 클론
```bash
git clone [레포지토리 주소]
cd [프로젝트 폴더]
```
## 2. 환경 설정
- MySQL 데이터베이스를 생성하고 `application.yaml` 파일에 접속 정보를 설정합니다.  
- `application.yaml`의 `spring.jpa.hibernate.ddl-auto`는 **update**로 설정되어 있어, 실행 시 자동으로 테이블이 생성됩니다.  


---

## 3. 애플리케이션 실행
IntelliJ 또는 IDE에서 `ZaritApplication.java`를 실행하거나, 터미널에서 아래 명령어를 실행합니다.  

```bash
./gradlew bootRun
```

## 📚 API 명세

### 1. 사용자 API
| 기능 | HTTP Method | 엔드포인트 | 설명 |
|------|-------------|------------|------|
| 로그인 | POST | `/login` | 이름으로 로그인 (신규/기존 구분) |
| 선호도 설정 | POST | `/login/{name}` | 사용자의 선호도 정보 저장 |

### 2. 퀘스트 API
| 기능 | HTTP Method | 엔드포인트 | 설명 |
|------|-------------|------------|------|
| 소음 퀘스트 | POST | `/quest/{name}/noise` | 소음 정보 입력 및 포인트 지급 |
| 와이파이 퀘스트 | POST | `/quest/{name}/wifi` | 와이파이 정보 입력 및 포인트 지급 |
| 콘센트 퀘스트 | POST | `/quest/{name}/plugbar` | 콘센트 정보 입력 및 포인트 지급 |
| 사진 퀘스트 | POST | `/quest/{name}/photo` | 사진 파일 업로드 및 포인트 지급 |

### 3. 장소(Pin) 정보 조회 API
| 기능 | HTTP Method | 엔드포인트 | 설명 |
|------|-------------|------------|------|
| 메인 페이지 | GET | `/mainpage/{name}` | 사용자 위치 기반 근처 핀 목록 조회 |
| 상세 정보 | GET | `/mainpage/{pin_name}` | 특정 핀의 상세 정보 조회 |
| 사진 목록 | GET | `/mainpage/{pin_name}/photos` | 핀의 사진 목록 조회 |

---

## 📦 핵심 클래스
- **UserService**: 사용자 로그인 및 `User_preference` 관리  
- **QuestService**: 퀘스트(noise, wifi 등) 로직 처리 및 `Pin_environment` 평균값 업데이트  
- **PinService**: 위치 기반 핀 조회, 상세 정보, 사진 목록 조회 기능 제공  
- **LocalFileStorageService**: 업로드된 파일을 서버 디스크에 저장하고 URL 반환  
- **GlobalExceptionHandler**: 모든 컨트롤러 예외를 통일된 `ApiResponse` 형식으로 처리  
