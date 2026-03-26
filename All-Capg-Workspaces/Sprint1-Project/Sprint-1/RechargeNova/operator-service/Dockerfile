FROM eclipse-temurin:17-jdk
ENV TZ=UTC
WORKDIR /app
COPY target/operator-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","app.jar"]