## build stage
FROM maven:3.9.5-eclipse-temurin-21-alpine AS builder

WORKDIR /build
COPY pom.xml .
# 1) dependency caching
RUN mvn -B dependency:resolve

# 2) source 복사 & package
COPY src/ /build/src/
RUN mvn package -DskipTests
RUN java -Djarmode=layertools -jar target/boot.jar extract

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /home/appuser

# 2) Jar 복사
COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/application/ ./

# 3) 실행
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]