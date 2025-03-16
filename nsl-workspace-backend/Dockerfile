# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven project files to the container
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application JAR
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/nsl-workspace-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port
EXPOSE 9000

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]