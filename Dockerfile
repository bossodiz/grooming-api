FROM eclipse-temurin:21-jdk-alpine
COPY target/grooming-api-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]