# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app


# Copy only pom first for better layer caching
COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline


# Then copy sources and build
COPY src ./src
RUN mvn -B -q -DskipTests package


# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /opt/app


# Security hardening (optional)
RUN useradd -r -s /bin/false appuser


COPY --from=build /app/target/*.jar app.jar
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080
USER appuser
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]