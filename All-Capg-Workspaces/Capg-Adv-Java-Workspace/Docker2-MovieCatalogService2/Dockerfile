FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY target/Docker2-MovieCatalogService2-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]