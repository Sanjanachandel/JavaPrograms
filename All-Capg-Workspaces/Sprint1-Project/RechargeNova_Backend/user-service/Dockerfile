# Stage 1: Build
FROM maven:3.8.4-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy parent pom and install it to the local repo (non-recursive)
# This avoids errors about missing child modules listed in the parent pom
COPY pom.xml .
RUN mvn install -N -DskipTests

# Copy only the service we want to build
COPY user-service ./user-service
WORKDIR /app/user-service

# Build the service
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]