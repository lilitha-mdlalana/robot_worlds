# Use the Maven base image with OpenJDK 17 and a slim configuration
FROM maven:3.8.5-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Add the precompiled JAR file to the container's /app directory
ADD libs/reference-server-0.2.3.jar /app/server.jar

# Expose port 8000 to allow connections to the server
EXPOSE 5050

# Create a script that will run the Java application
RUN echo 'java -jar /app/server.jar' > /app/script.sh && \
    chmod +x /app/script.sh

# Specify the command to run when the container starts
CMD ["/app/script.sh"]