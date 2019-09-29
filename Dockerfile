FROM maven:latest
COPY . /app
WORKDIR app
RUN mvn package

FROM openjdk:11-jre-slim
COPY --from=0 /app/target/zqxjkcrud-master.jar /
ENTRYPOINT java $JAVA_ARGS -jar /zqxjkcrud-master.jar
