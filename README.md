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

# DATABASE SELECTION

```
$ mvn spring-boot:run \
        -Dspring-boot.run.arguments="\
        --spring.datasource.url=<JDBC_URL> \
        --spring.datasource.driver-class-name=<DRIVER_CLASS> \
        --zqxjk.schema=<SCHEMA_WITH_ZQXJK>"
```

# OpenID Connect with Ipsilon

Note that by default, Ipsilon doesn't allow non-https redirect URIs.

Patch `/usr/lib/python3.9/site-packages/ipsilon/providers/openidc/provider.py` here https://pagure.io/ipsilon/blob/master/f/ipsilon/providers/openidc/provider.py#_143 so that you can test easily.

Use the following environment variables:

```
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_IPSILON_CLIENT_ID=bar
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_IPSILON_CLIENT_SECRET=LKuwnNadEiUfAc55CQAPuqFwP3kQIqpT
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_IPSILON_SCOPE=openid,profile
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_IPSILON_REDIRECT_URI=APPLICATION_BASE/login/oauth2/code/login-client
SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_IPSILON_ISSUER_URI=BASE_IPSILON_URL/openidc/
```

# HACKING

Run tests (including integration tests) using `mvn test`.
