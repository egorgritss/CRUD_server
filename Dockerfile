FROM openjdk:15-jdk AS app-build

ENV GRADLE_OPTS -Dorg.gradle.daemon=false
COPY . /build
WORKDIR /build
RUN chmod -R 777 ./gradlew
RUN ./gradlew build