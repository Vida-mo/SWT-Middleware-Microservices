FROM bellsoft/liberica-openjdk-alpine:21
#FROM bellsoft/liberica-openjdk-debian:21


ARG JAR_FILE=target/my-oa-all-gt-resource-0.0.1-SNAPSHOT.jar
#ARG JAR_FILE=build/lib/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
