# Use the official Tomcat image as a base
FROM tomcat:10.1-jdk17-openjdk-slim

# Remove the default webapps to keep it clean
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your war file into the Tomcat webapps directory
COPY the_tracking_guard_dog-0.0.1-SNAPSHOT-plain.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]