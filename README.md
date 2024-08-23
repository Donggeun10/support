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

## 4. Docker Image 생성 명령어

```
docker build -t demo-api:local .  && docker run -p 9090:8080  -e"SPRING_PROFILES_ACTIVE=local"  demo-api:local
```

## 5. 주요 문제 정의 및 해결 전략

