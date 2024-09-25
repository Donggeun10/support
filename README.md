# Backend Server 주요 개발 내용
- Spring Boot 기반의 RESTful API 서버 구현
- RestControllerAdvice 와 ExceptionHandler 를 이용한 예외 처리
- H2 Database 를 이용한 데이터 저장 및 조회
- Spring Data JPA 를 이용한 데이터 CRUD
- LockModeType.PESSIMISTIC_WRITE 를 이용한 데이터 동시성 제어
- Springdoc-openapi 를 이용한 API 문서화
- Lombok 을 이용한 코드 간소화
- Docker 를 이용한 이미지 생성 및 실행
- Aspect 와  @Around 를 이용한 로깅

## 1. Frameworks And Tools
- JDK 21
- spring-boot 3.3.2
- spring-boot-web
- spring-boot-data-jpa
- spring-boot-security
- spring-boot-cache
- ehcache
- springdoc-openapi
- h2database
- lombok

## 2. Api Spec 확인 주소
- http://localhost:8080/swagger-ui.html

## 3. 주요 Endpoints

- GET /api/v1/announcements
- GET /api/v1/announcements/page
- GET /api/v1/announcement/id/{id}
- POST /api/v1/announcement 
- POST /api/v1/announcement/id/{id}
- PUT /api/v1/announcement/id/{id} 
- DELETE /api/v1/announcement/id/{id}
- DELETE /api/v1/announcement/id/{id}/file-id/{fileId}

## 4. Docker Image 생성 및 실행 명령어 

```
docker build -t demo-api:local .  && docker run -p 9090:8080  -e"SPRING_PROFILES_ACTIVE=local"  demo-api:local
docker build -t demo-api:local -f Dockerfile_git . && docker run -p 9090:8080  -e"SPRING_PROFILES_ACTIVE=local"  demo-api:local
docker build -t demo-api:local -f Dockerfile_aot . && docker run -p 9090:8080  -e"SPRING_PROFILES_ACTIVE=local"  demo-api:local
```

## 5. 주요 문제 정의 및 해결 전략
- 동시에 같은 공지 사항을 변경 하거나 읽는 요청이 다수일 때에 대한 문제
  - DB 테이블 내 lock 을 통해 변경 중 타 세션의 접근을 제한 하도록 적용
- 데이터 조회 요청이 많은 문제
  - virtual thread 적용을 통한 비동기 처리 
  - jdk.virtualThreadScheduler.parallelism 를 통한 병렬 처리
  - 캐시를 통해서 반복적으로 같은 데이터에 대한 요청에 대한 DB 조회를 최소화 함
- 보안 문제
  - spring security를 통한 범용적 보안 설정 적용
  - basic authentication 을 통해서 인증된 사용자만 API에 접근 가능 하도록 적용
- 인스턴스 시작 시간 및 라이브러리 로딩 문제
  - AOT 컴파일을 통한 이미지 생성을 통해 인스턴스 시작 시간을 최소화

## 6. 테스트 계정 및 방법
- 테스트 계정
  - robot / play
  - sam / ground
- 방법
  - swagger-ui.html 에서 접속 후 테스트
