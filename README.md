# Spring boot multi-module skeleton
Skeleton construct of Spring boot based REST API application. Simple
use case is included for implementation reference.

## Technical stacks/libs used:
 - Java 1.8
 - [Kotlin](https://kotlinlang.org/) 1.1
 - [Gradle](https://gradle.org/) 4.x
 - [Spring boot](http://spring.io/projects/spring-boot) 2
   * Externalised configurations
   * I18n support
 - [Undertow](http://undertow.io/)
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

## How to set up a SSL
This project is intended to run as stand-alone HTTPS server with
Let's encrypt certificate. However, due to the nature of Java Keystore
system, [server restart is required](https://github.com/spring-projects/spring-boot/issues/5450)
after the certificate expires. To avoid this problem, it is better to
setup a reverse proxy with well known HTTP daemons such as `nginx` or
`httpd`.

There are some demo scripts under `settings/letsencrypt` directory for it, and customise it at your own needs.

## TO-DOs
- Error handlers
- JPA
- Unit test with JUnit5 Test code
- Integration test
- Static analysis
- Coverage report
- Spring security
- FCMClient externalisation
