# Multi-stage build for Spring Boot application
FROM maven:3.9-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/prayer-app-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"] 