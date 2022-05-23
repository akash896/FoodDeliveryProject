FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} h2.jar
COPY target/initialData.txt initialData.txt
EXPOSE 8083
ENTRYPOINT ["java","-jar", "/h2.jar"]
