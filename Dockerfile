FROM docker.io/library/rust:latest as rust_builder
WORKDIR /usr/src/myapp
COPY manifest-builder .
RUN CARGO_REGISTRIES_CRATES_IO_PROTOCOL=sparse cargo install --path .

FROM docker.io/library/maven:latest as java_builder
COPY pom.xml /app/pom.xml
COPY src /app/src

WORKDIR app
RUN mvn -B package -Dmaven.test.skip=true

FROM docker.io/library/eclipse-temurin:17
ARG QUAY_EXPIRES_AFTER=never
LABEL quay.expires-after=$QUAY_EXPIRES_AFTER
COPY --from=java_builder /app/target/zqxjkcrud-master.jar /
COPY --from=rust_builder /usr/local/cargo/bin/zqxjkcrud-manifest-builder /usr/local/bin/zqxjkcrud-manifest-builder
ENTRYPOINT ["sh", "-c", "/opt/java/openjdk/bin/java $JAVA_ARGS -jar /zqxjkcrud-master.jar $ZQXJK_OPTS"]
