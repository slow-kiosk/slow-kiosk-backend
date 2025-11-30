# 1. Base Image (Java 17 버전 사용)
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일 복사 (프로젝트 빌드 후 생성된 jar 경로 확인 필요)
# 보통 build/libs/ 안에 생성됩니다.
COPY build/libs/*SNAPSHOT.jar app.jar

# 4. 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]