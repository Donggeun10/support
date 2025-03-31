## build stage
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build
COPY pom.xml .
# 1) dependency caching
RUN mvn -B dependency:resolve

# 2) source 복사 & package
COPY src/ /build/src/
RUN mvn package -DskipTests
RUN java -Djarmode=tools -jar target/boot.jar extract --destination application

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /home/appuser

# 2) Jar 복사
COPY --chown=appuser --from=builder /build/application/lib ./lib
COPY --chown=appuser --from=builder /build/application/boot.jar ./boot.jar

# 3) 실행
ENTRYPOINT ["java", "-Djdk.tracePinnedThreads=full", "-Djdk.virtualThreadScheduler.parallelism=10", "-jar", "boot.jar"]