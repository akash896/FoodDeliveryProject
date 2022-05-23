FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} restaurant.jar
ENTRYPOINT ["java","-jar", "/restaurant.jar"]
