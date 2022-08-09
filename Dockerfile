FROM openjdk:16.0-buster
VOLUME /tmp
EXPOSE 9666
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
CMD java -Xms256m -Xmx512m -jar app.jar