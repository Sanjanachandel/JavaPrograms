FROM eclipse-temurin:17-jdk
ENV TZ=UTC
WORKDIR /app
COPY target/gateway-service.jar app.jar
EXPOSE 8989
ENTRYPOINT ["java","-jar","app.jar"]