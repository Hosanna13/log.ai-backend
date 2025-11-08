# 1. Use an official JDK base image
FROM eclipse-temurin:17-jdk-alpine

# 2. Set working directory inside container
WORKDIR /app

# 3. Copy Maven wrapper & project files
COPY . .

# 4. Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# 5. Expose the app port (same as in .env)
EXPOSE 8080

# 6. Run the jar file
ENTRYPOINT ["java","-jar","target/backend-0.0.1-SNAPSHOT.jar"]