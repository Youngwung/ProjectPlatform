# backend/Dockerfile

FROM openjdk:17-jdk-slim

WORKDIR /app

# Gradle 빌드 파일 복사

COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle

# 소스 코드 복사

COPY src ./src

# 프로젝트 빌드

RUN ./gradlew build -x test

EXPOSE 8080

CMD ["java", "-jar", "build/libs/backend-0.0.1-SNAPSHOT.jar"]
