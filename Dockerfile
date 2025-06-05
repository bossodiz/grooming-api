# ใช้ JDK 21
FROM eclipse-temurin:21-jdk-alpine

# ตั้ง working directory
WORKDIR /app

# copy jar จาก deploy (คุณต้อง build มาก่อน)
COPY deploy/grooming-api-1.0.jar app.jar

# ให้ Spring Boot ใช้ profile=prod และอ่านพอร์ตจาก Railway
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod", "--server.port=${PORT}"]
