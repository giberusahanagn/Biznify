# Use lightweight Alpine image with OpenJDK 17
FROM openjdk:17-jdk-alpine

# Expose the default Spring Boot port (optional, useful for docs and Kubernetes)
EXPOSE 8080

# Add non-root user for better security (optional)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR (not the .original one)
COPY target/inbound-shipment-service.jar app.jar

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
