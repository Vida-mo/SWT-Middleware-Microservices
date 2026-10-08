#FROM bellsoft/liberica-openjdk-alpine:17-cds
#azul/zulu-openjdk-debian:17-latest
FROM amazoncorretto:21

ARG JAR_FILE=target/registry*SNAPSHOT.jar
#ARG JAR_FILE=target/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

#HEALTHCHECK CMD curl --fail http://localhost:8080 || exit 1

ENTRYPOINT ["java","-jar","/app.jar"]