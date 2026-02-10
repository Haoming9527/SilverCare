# Use Tomcat 10 which supports Jakarta EE 10 (Servlets 6.0, JSP 3.1)
FROM tomcat:10.1-jdk21-temurin

# Remove default Tomcat apps to clean up
RUN rm -rf /usr/local/tomcat/webapps/*

# Disable the shutdown port to prevent Render's health check from hitting it
RUN sed -i 's/port="8005" shutdown="SHUTDOWN"/port="-1" shutdown="SHUTDOWN"/' /usr/local/tomcat/conf/server.xml

# Copy the webapp content into the ROOT context
COPY src/main/webapp /usr/local/tomcat/webapps/ROOT

EXPOSE 8080

CMD ["catalina.sh", "run"]
