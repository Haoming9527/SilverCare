# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
COPY . /app
WORKDIR /app
# Skip tests to speed up the build on Render
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.war /app/app.war
EXPOSE 8081
# Run as a Spring Boot executable WAR
ENTRYPOINT ["java", "-jar", "/app/app.war"]
