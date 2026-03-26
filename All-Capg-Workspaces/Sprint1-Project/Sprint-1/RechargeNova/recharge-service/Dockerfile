FROM eclipse-temurin:17-jdk

ENV TZ=UTC

WORKDIR /app

# ✅ Correct JAR name
COPY target/recharge-service.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java","-jar","app.jar"]