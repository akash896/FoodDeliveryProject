FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} delivery.jar
COPY target/initialData.txt initialData.txt
EXPOSE 8081
ENTRYPOINT ["java","-jar", "/delivery.jar"]
