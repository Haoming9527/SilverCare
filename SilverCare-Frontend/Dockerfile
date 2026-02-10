# Use Tomcat 10 which supports Jakarta EE 10 (Servlets 6.0, JSP 3.1)
FROM tomcat:10.1-jdk21-slim

# Remove default Tomcat apps to clean up
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the webapp content into the ROOT context or a specific context path
# Here we use the project name as the context path to match existing URLs
COPY src/main/webapp /usr/local/tomcat/webapps/SilverCare-Frontend

EXPOSE 8080

CMD ["catalina.sh", "run"]
