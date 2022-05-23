FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} restaurant.jar
COPY target/initialData.txt initialData.txt
EXPOSE 8080
ENTRYPOINT ["java","-jar", "/restaurant.jar"]
