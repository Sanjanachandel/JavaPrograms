# Stage 1: Build
FROM maven:3.8.4-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Install parent pom
COPY pom.xml .
RUN mvn install -N -DskipTests

# Build service
COPY recharge-service ./recharge-service
WORKDIR /app/recharge-service
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/recharge-service/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]