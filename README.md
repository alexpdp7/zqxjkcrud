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

# HACKING

Run tests (including integration tests) using `mvn test`.

# SAML

```
$ openssl genrsa -out localhost.key 2048
$ openssl req -new -x509 -key localhost.key -out localhost.pem -days 3650 -subj /CN=localhost
$ openssl pkcs8 -topk8 -inform PEM -outform DER -in  localhost.key -out  localhost.key.der -nocrypt
```

```
$ mvn spring-boot:run -Dspring-boot.run.arguments="\
	--spring.security.saml2.relyingparty.registration.foo.signing.credentials.private-key-location=file:./localhost.key.der \
	--spring.security.saml2.relyingparty.registration.foo.signing.credentials.certificate-location=file:./localhost.pem \
	--spring.security.saml2.relyingparty.registration.foo.assertingparty.metadata-uri=https://foo.bar/ \
```
