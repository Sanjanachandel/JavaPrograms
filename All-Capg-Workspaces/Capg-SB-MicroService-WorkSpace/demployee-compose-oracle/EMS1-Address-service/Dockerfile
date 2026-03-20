FROM eclipse-temurin:17-jdk
ENV TZ=UTC
WORKDIR /app
COPY target/EMS1-Address-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7074
ENTRYPOINT ["java","-jar","app.jar"]