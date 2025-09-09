# ===== Build stage (มี Maven ให้แล้ว) =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# copy pom.xml ก่อนเพื่อ cache dependency
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# copy src แล้ว build
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ===== Runtime stage (ใช้ JRE เบา ๆ) =====
FROM eclipse-temurin:21-jre
ENV TZ=Asia/Bangkok
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
