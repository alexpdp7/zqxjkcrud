FROM docker.io/library/maven:latest
COPY . /app
WORKDIR app
RUN mvn -B package -Dmaven.test.skip=true

FROM docker.io/library/amazoncorretto:17
COPY --from=0 /app/target/zqxjkcrud-master.jar /
ENTRYPOINT ["sh", "-c", "/usr/bin/java $JAVA_ARGS -jar /zqxjkcrud-master.jar $ZQXJK_OPTS"]
