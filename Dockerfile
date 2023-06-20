FROM openjdk:17-alpine3.12
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/jarvis-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} jarvis.jar
ENTRYPOINT ["java","-jar","/jarvis.jar"]