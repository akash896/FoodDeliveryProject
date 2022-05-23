FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} wallet.jar
ENTRYPOINT ["java","-jar", "/wallet.jar"]
