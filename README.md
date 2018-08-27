# Spring boot multi-module skeleton
Skeleton construct of Spring boot based REST API application. Simple
use case is included for implementation reference.

## Technical stacks/libs used:
  - Build tools:
    * [Gradle](https://gradle.org/) 4.x
  - Languages:
    * Java 1.8 - platform runtime
    * [Kotlin](https://kotlinlang.org/) 1.2 - implementation, unit test
    * [Groovy](http://groovy-lang.org/) 2.4 - integration test
  - Frameworks and runtimes:
    * [Undertow](http://undertow.io/) - for Web application server
    * [Spring boot](http://spring.io/projects/spring-boot) 2 - application framework
    * JPA with [Hibernate](http://hibernate.org/) - persistent data
    * [HicariCP](https://github.com/brettwooldridge/HikariCP) - database connection pool
    * [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/) - push message service
  - Code quality:
    * [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
    * [Spock framework](http://spockframework.org/) with [Groovy](http://groovy-lang.org/)
    * [detekt](https://arturbosch.github.io/detekt/index.html)

## How to build
```
./gradlew clean deploy [writeVersionFile] [-PbuildConfig={DEV|ALPHA|BETA|RELEASE}]
```
- Build target will be defaulted as DEV if the option is omitted.
- `writeVersionFile` task will generate a file named `version.properties`
  on project root directory.
- Standalone executable JAR file will be created in `build/outputs`
  directory after `deploy` task is finished.

## How to run
```
java -jar `ls build/outputs/*.jar`
```
- Or just double click the JAR file in `build/outputs` at your desktop environment.

## Invoking `Hello, world`
GET request and response
```
$ curl --insecure \
  -X GET \
  -H "content-type: application/json" \
  https://localhost:8080/hello
{
  "body": "GET Hello, world",
  "type": "OK"
}
```

POST request and response
```
$ curl --insecure \
  -X POST \
  -H "content-type: application/json" \
  -d '{"name":"FrancescoJo"}' \
  https://localhost:8080/hello
{
  "body": "POST Hello, FrancescoJo",
  "type": "OK"
}
```

## Error handling
All logical problems are directed to `com.github.fj.restapi.endpoint.CustomErrorHandler` first,
and converted as `com.github.fj.restapi.dto.ErrorResponse` for *consistent* response handling.
The error response will be look like:

```
{
  "body": {
    "message": "Not Found",
    "reason": "GeneralHttpException"
  },
  "type": "ERROR"
}
```

Without this `@ControllerAdvice`, all programme exceptions will be handled at
`org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController#error` by default,
which produces error as follows:

```
{
  "timestamp": "2018-08-23T02:15:48.527+0000",
  "status": 404,
  "error": "Not Found",
  "message": "Not Found",
  "path": "/"
}
```

Looks quite different to our custom OK/ERROR responses.

## How to set up a SSL
This project is intended to run as stand-alone HTTPS server with
Let's encrypt certificate. However, due to the nature of Java Keystore
system, [server restart is required](https://github.com/spring-projects/spring-boot/issues/5450)
after the certificate expires. To avoid this problem, it is better to
setup a reverse proxy with well known HTTP daemons such as `nginx` or
`httpd`.

Setting operation mode as SSL is dead simple, just create a jks key by [`keytool`](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)
command or import a CA certificate and convert it by [`openssl`](https://www.openssl.org/docs/man1.0.2/apps/openssl.html),
and change your `application.yml` as follows:

```
# Setting 'true' will accept HTTPS requests only
security:
  require-ssl: true

server:
  ssl:
    enabled: true
```

There are some demo scripts under `settings/letsencrypt` directory for it, and customise it at your own needs.

## How to run tests and create report files
```
./gradlew test integrationTest jacocoTestReport
```
- In contrast to `test` task, `integrationTest` is required to be run under a separated environment.
  Please check files under `_application/src/integrationTest/resources` directory for an example.

## How to run static analysis and create report files
```
./gradlew detekt
```
- This project uses [detekt](https://arturbosch.github.io/detekt/index.html) as a static analyser.
  For your own configuration, read the official document and modify `gradle/scripts/static-analysis-detekt.gradle` file.

## TO-DOs
- Swagger integration
- Spring security
- Docker integration + Local database environment
- Make this works on eclipse
