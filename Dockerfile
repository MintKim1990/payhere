# AdoptOpenJDK 17 기반 이미지
FROM openjdk:17-jdk-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 복사
COPY gradlew .
COPY gradle gradle

# Gradle 설정 파일 복사
COPY build.gradle.kts .
COPY settings.gradle.kts .

# 소스 코드 복사
COPY src src

# Gradle을 사용하여 애플리케이션 빌드
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# 실행을 위한 새로운 이미지 생성
FROM openjdk:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드한 JAR 파일을 복사
COPY --from=builder /app/build/libs/*.jar ./app.jar

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]
