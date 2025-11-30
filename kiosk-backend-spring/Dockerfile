# 1. 빌드 환경 (Build Stage)
FROM openjdk:17-jdk-slim AS builder
WORKDIR /build

# Gradle 파일만 먼저 복사 (캐시 활용)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
# 권한 부여
RUN chmod +x gradlew

# 소스 복사 및 빌드 (테스트 제외)
COPY src src
RUN ./gradlew clean bootJar -x test

# 2. 실행 환경 (Run Stage) - 가벼운 이미지 사용
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일만 복사
COPY --from=builder /build/build/libs/*SNAPSHOT.jar app.jar

# 실행
ENTRYPOINT ["java", "-jar", "app.jar"]