FROM gradle:8.0-jdk-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src/producer
WORKDIR /home/gradle/src/producer
RUN gradle bootJar --no-daemon --stacktrace

FROM openjdk:16.0-buster
VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar
EXPOSE 9666
COPY --from=build /home/gradle/src/producer/build/libs/*.jar app.jar
CMD java -Xms256m -Xmx512m -jar /app.jar