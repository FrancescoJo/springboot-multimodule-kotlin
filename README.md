# Spring boot multi-module skeleton
Skeleton construct of Spring boot based REST API application. Simple
use case is included for implementation reference.

## Technical stacks/libs used:
  - Java 1.8
  - [Kotlin](https://kotlinlang.org/) 1.2
  - [Gradle](https://gradle.org/) 4.x
  - [Spring boot](http://spring.io/projects/spring-boot) 2
    * Externalised configurations
    * I18n support
  - [Undertow](http://undertow.io/)
  - JPA with [Hibernate](http://hibernate.org/)
  - [HicariCP](https://github.com/brettwooldridge/HikariCP)
  - [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/)

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

Looks quite different to OK responses.

## How to set up a SSL
This project is intended to run as stand-alone HTTPS server with
Let's encrypt certificate. However, due to the nature of Java Keystore
system, [server restart is required](https://github.com/spring-projects/spring-boot/issues/5450)
after the certificate expires. To avoid this problem, it is better to
setup a reverse proxy with well known HTTP daemons such as `nginx` or
`httpd`.

There are some demo scripts under `settings/letsencrypt` directory for it, and customise it at your own needs.

## Package naming and structure

## How to run tests and its results
```
./gradlew [test] [integrationTest]
```

## TO-DOs
- Unit test with JUnit5 Test code
- Integration test
- Static analysis
- Coverage report
- Swagger integration
- Docker integration + Local database environment
- Spring security
- FCMClient externalisation
