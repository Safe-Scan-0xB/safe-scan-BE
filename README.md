<a href="https://club-project-one.vercel.app/" target="_blank">
<img src="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNTAzMjVfMjEw%2FMDAxNzQyOTA1NjA5ODE2.Q-lhLkYxbFUcCCRvpGJpyVQT4p4b0VfgFdp7cBb7dEsg.bhexs38yN9Z-pZ8_eT_esj0y-rzQZ6MzE_2Kyr1-y88g.JPEG%2FKakaoTalk_20250325_212609816.jpg&type=sc960_832" alt="배너" width="70%"/>
</a>
<br/>
<br/>
# 1. Project Overview (프로젝트 개요)<br>
- 프로젝트 이름: Safe-Scan<br>
- 프로젝트 설명: SafeScan 은 공공 데이터와 AI 분석, 국민 참여 제보를 기반으로 수상한 접촉을 검증하고 위험을 확인할 수 있는 플랫폼입니다.<br> 사용자가 위험을 감지하고, 정보를 공유, 확인하여 범죄로 이어지기 전에 차단할 수 있도록 지원합니다.
<br/>
<br/>
# 2. BE Team Members (팀원 및 팀 소개)<br>
| 최희선 | 전수연 | 김민서 |<br>
|:------:|:------:|:------:|<br>
| <img src="https://github.com/user-attachments/assets/c1c2b1e3-656d-4712-98ab-a15e91efa2da" alt="최희선" width="150"> | <img src="https://github.com/user-attachments/assets/78ec4937-81bb-4637-975d-631eb3c4601e" alt="전수연" width="150"> | <img src="https://github.com/user-attachments/assets/78ce1062-80a0-4edb-bf6b-5efac9dd992e" alt="김민서" width="150"> |<br>
| BE | BE | BE |<br>
| [GitHub](https://github.com/todaysunny612) | [GitHub](https://github.com/ssuisig) | [GitHub]() |

<br/>
<br/>

# 3. Key Features (주요 기능)

- **로그인**:
  - 사용자 인증 정보를 통해 로그인합니다.

- **악성 링크 실시간 위험도 조회**:
  - KISA에서 제공하는 공공데이터 API를 활용하여 악성 URL을 실시간 검증할 수 있습니다.
  - 신고 이력 데이터 기반 ‘안전/위험’ 등급을 제공합니다.

- **커뮤니티 (사례 공유, 피해자 찾기) 기능**:
  - 카테고리 기반으로 정보를 탐색 (피해자 찾기, 사례 공유 등)할 수 있습니다.
  - HOT 게시판 제공 → 현재 가장 활발한 이슈를 즉시 파악할 수 있습니다.

- **AI 챗봇 (사전 예방, 사후 대처 안내)**:
  - 자유로운 질의응답 → 상황설명 중심 접근 가능
  - 위험 요소가 감지되면 명확하게 경고하고 사용자가 바로 실천할 수 있는 행동을 제시하도록 시스템 프롬프트를 적용하여 설계함


<br/>
<br/>

# 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 최희선    |  <img src="https://github.com/user-attachments/assets/c1c2b1e3-656d-4712-98ab-a15e91efa2da" alt="최희선" width="100"> | <ul><li>공공데이터 API 연결</li><li>AI 챗봇 API 연결</li></ul>     |
| 전수연   |  <img src="https://github.com/user-attachments/assets/78ec4937-81bb-4637-975d-631eb3c4601e" alt="전수연" width="100">| <ul><li>커뮤니티 기능 구현</li><li>채팅 DB 구현</li></ul> |
| 김민서   |  <img src="https://github.com/user-attachments/assets/78ce1062-80a0-4edb-bf6b-5efac9dd992e" alt="김민서" width="100">    |<ul><li>로그인 기능 구현</li><li>서비스 배포</li></ul>  |

<br/>
<br/>
# 5. Technology Stack (기술 스택)<br>
## 5.1 Language<br>
|  |  |<br>
|-----------------|-----------------|<br>
| Java    |<img src="https://github.com/user-attachments/assets/5ed8f401-9129-4ae4-abb5-abefc9f18951" alt="Java" width="100"/>| 


<br/>

## 5.3 Backend
|  |  |  |
|-----------------|-----------------|-----------------|
| Springboot    |  <img alt="springbootImage2" src="https://github.com/user-attachments/assets/e70e91ac-a3f1-4e45-a4f9-7555b008593b"  alt="Springboot" width="100"/>   | 10.12.5    |


<br/>

# 6. Project Structure (프로젝트 구조)
```plaintext
Safe-Scan/
├── .gradle/                        # Gradle 빌드 캐시 디렉토리
├── build/                          # 빌드 결과물 디렉토리
├── gradle/                         # Gradle wrapper 설정
│   └── ... 
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/springboot/safescan/
│   │   │       ├── config/         # 전역 설정 (CORS, Security, Swagger 등)
│   │   │       ├── controller/     # REST API 엔드포인트
│   │   │       ├── domain/         # 엔티티(DB 모델)
│   │   │       ├── dto/            # 데이터 전송 객체
│   │   │       ├── mapper/         # Entity ↔ DTO 변환 매퍼
│   │   │       ├── openAI/         # GPT API 연동 클래스
│   │   │       ├── prompt/         # 시스템/기능 프롬프트 분리 관리
│   │   │       ├── repository/     # JPA Repository
│   │   │       ├── security/       # JWT, 인증·인가
│   │   │       ├── service/        # 비즈니스 로직
│   │   │       ├── util/           # 공용 유틸리티
│   │   │       └── SafeScanApplication.java   # Spring Boot 메인 애플리케이션
│   │   └── resources/
│   │       ├── prompts/            # 프롬프트 텍스트 파일
│   │       └── application.yml     # 환경 설정 파일
│   └── test/                       # 테스트 코드
├── uploads/                        # 이미지/파일 업로드 저장 디렉토리
├── .env                            # 환경 변수 파일 (Git에서 제외 권장)
├── .gitattributes
├── .gitignore
├── build.gradle                    # Gradle 빌드 스크립트
├── gradlew                         # Unix용 Gradle 실행 파일
├── gradlew.bat                     # Windows용 Gradle 실행 파일
├── HELP.md
├── settings.gradle                 # 멀티 프로젝트 및 Gradle 설정
└── package.json                    # 필요 시 Node 기반 스크립트 / 툴링 사용
```

<br/>
<br/>
# 7. Development Workflow (개발 워크플로우)
## 브랜치 전략 (Branch Strategy)
우리의 브랜치 전략은 Git Flow를 기반으로 하며, 다음과 같은 브랜치를 사용합니다.
- Main Branch
  - 배포 가능한 상태의 코드를 유지합니다.
  - 모든 배포는 이 브랜치에서 이루어집니다.

- develop Branch
  - 서비스 개발의 기준(branch base) 역할을 합니다.
  - 팀원들의 기능 브랜치가 모두 병합되는 통합 개발 브랜치입니다.
  - 기능 구현이 완료되면 PR을 생성하여 코드 리뷰 후 머지합니다.
  - Main으로 병합되기 전 최종 테스트 및 버그 수정이 이루어집니다.

- {name} Branch
  - 팀원 각자의 개발 브랜치입니다.
  - 모든 기능 개발은 이 브랜치에서 이루어집니다.

<br/>
<br/>
