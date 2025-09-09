# ---- Build stage: Maven + JDK 21 ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -q -f pom.xml dependency:go-offline

COPY src ./src
RUN mvn -B -q -f pom.xml clean package -DskipTests

# ---- Run stage: JRE 21 ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
