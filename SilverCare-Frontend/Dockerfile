# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM tomcat:10.1-jdk21-temurin

# Clean up default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Disable the shutdown port to prevent Render's health check from hitting it
RUN sed -i 's/port="8005" shutdown="SHUTDOWN"/port="-1" shutdown="SHUTDOWN"/' /usr/local/tomcat/conf/server.xml

# Copy the built WAR from the build stage to the ROOT context
COPY --from=build /app/target/SilverCare-Frontend.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
