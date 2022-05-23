FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} wallet.jar
COPY target/initialData.txt initialData.txt
EXPOSE 8082
ENTRYPOINT ["java","-jar", "/wallet.jar"]
