FROM maven:latest
COPY . /app
WORKDIR app
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:11-jre-slim
COPY --from=0 /app/target/zqxjkcrud-master.jar /
ENTRYPOINT ["sh", "-c", "/usr/local/openjdk-11/bin/java $JAVA_ARGS -jar /zqxjkcrud-master.jar $ZQXJK_OPTS"]
