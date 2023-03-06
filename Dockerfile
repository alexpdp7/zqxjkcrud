FROM maven:latest
COPY . /app
WORKDIR app
RUN mvn -B package -Dmaven.test.skip=true

FROM openjdk:17-jre-slim
COPY --from=0 /app/target/zqxjkcrud-master.jar /
ENTRYPOINT ["sh", "-c", "/usr/local/openjdk-17/bin/java $JAVA_ARGS -jar /zqxjkcrud-master.jar $ZQXJK_OPTS"]
