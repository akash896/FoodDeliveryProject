FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} delivery.jar
ENTRYPOINT ["java","-jar", "/delivery.jar"]
