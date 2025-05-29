# -------- Build Stage --------
FROM gradle:8.5-jdk17 AS builder

# Create app directory
WORKDIR /app

# Copy all files
COPY . .

# Build the application (fat jar)
RUN gradle clean build -x test

# -------- Run Stage --------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]