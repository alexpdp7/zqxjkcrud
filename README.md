# DEMO

You need to use Java 17.

```
$ mvn spring-boot:run
```

This will start the application with a Docker test database defined at `src/main/resources/sample/schema.sql` with credentials admin/admin.

For this to run under podman:

```
$ systemctl --user enable podman.socket --now
$ export DOCKER_HOST=unix:///run/user/${UID}/podman/podman.sock
$ export TESTCONTAINERS_RYUK_DISABLED=true
```

To use Java 17 on EL9:

```
$ JAVA_HOME=/usr/lib/jvm/jre-17/ mvn ...
```

# DATABASE SELECTION

```
$ mvn spring-boot:run \
        -Dspring-boot.run.arguments="\
		--spring.datasource.url=<JDBC_URL> \
		--spring.datasource.driver-class-name=<DRIVER_CLASS> \
                --zqxjk.schema=<SCHEMA_WITH_ZQXJK>"
```
