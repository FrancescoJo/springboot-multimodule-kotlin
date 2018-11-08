# Spring boot + Kotlin multi-module demo
Skeleton construct of Spring boot based REST API application. Simple use case is included for
implementation reference.

Although the framework setup difficulty has reduced after release of Spring boot, configuring
Spring framework is still not an easy task. This template project is for reducing such costs
at the initial development stage.

This project requires IntelliJ IDEA since the Gradle/Kotlin of Eclipse is not working properly
(current: Oct. 2018).

## Technical stacks/libs used:
  - Build tools:
    * [Gradle](https://gradle.org/) 4.x
  - Languages:
    * Java 1.8 - platform runtime
    * [Kotlin](https://kotlinlang.org/) 1.3 - implementation, unit test
    * [Groovy](http://groovy-lang.org/) 2.5 - integration test
  - Frameworks and runtimes:
    * [Undertow](http://undertow.io/) - for Web application server
    * [Spring boot](http://spring.io/projects/spring-boot) 2 - application framework
    * [Hibernate](http://hibernate.org/) - JPA / persistent data
    * [HicariCP](https://github.com/brettwooldridge/HikariCP) - database connection pool
    * [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/) - message push service
  - Code quality:
    * [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
    * [Spock framework](http://spockframework.org/) with [Groovy](http://groovy-lang.org/)
    * [detekt](https://arturbosch.github.io/detekt/index.html)
    * [mockito-kotlin](https://github.com/nhaarman/mockito-kotlin)
  - Automatic documentation:
    * [Swagger](https://swagger.io/) - API document generator
    * [Swagger springfox](http://springfox.github.io/springfox/) - Swagger integration with Spring boot

## How to build
```
./gradlew clean deploy [writeVersionFile] [-PbuildConfig={DEV|ALPHA|BETA|RELEASE}]
```
- `buildConfig` will be defaulted as `DEV` if the option is omitted.
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
  "body": {
    "message": "GET Hello, world"
  },
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
  "body": {
    "message": "POST Hello, FrancescoJo"
  },
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
    "reason": "Resource /hello is not found."
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

looks quite different to our custom OK/ERROR responses.

## Authentication

This demo shows a custom token-based authentication scheme. This demo assumes that an user 
information must be provided to use security protected function, therefore even with a JWT token
we still need to find from wherever the user information is stored.

Using Spring security gives advantages still though, that we can allow or deny by their own roles
without writing `if - else` checks on every handler methods.

## How to setup a SSL
This project is constructed to run as stand-alone HTTPS server with
Let's encrypt certificate. However, due to the nature of Java Keystore
system, [server restart is required](https://github.com/spring-projects/spring-boot/issues/5450)
after the certificate expires. To avoid this problem, it is better to
setup a reverse proxy with well known HTTP daemons such as `nginx` or
`httpd`. Moreover, a good point of utilising such services is, restarting
it is much faster than Spring application. Therefore, if you can configure
reverse proxy, setup it only on the reverse proxy side and run this
project as HTTP mode.

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
- Logging with AOP
- Bugfix on FCMClient / Migrate from GSON to Jackson
- Make Pre/Post authorise works properly
- Make Create - Get - Login - Delete scenario test
- Fix all tests
- JWT Token support
- Flatten all commit history
- non-JPA implementation
- More complex project model on another branch
- Docker integration
