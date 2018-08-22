# Spring boot multi-module skeleton
Skeleton construct of Spring boot based REST API application. Simple
use case is included for implementation reference.

This project is intended to run as stand-alone HTTPS server with
Let's encrypt certificate. However, due to the nature of Java Keystore
system, [server restart is required](https://github.com/spring-projects/spring-boot/issues/5450)
after the certificate expires. To avoid this problem, it is better to
setup a reverse proxy with well known HTTP daemons such as `nginx` or
`httpd`.

## How to build and run
```
./gradlew clean deploy [writeVersionFile] [-PbuildConfig={DEV|ALPHA|BETA|RELEASE}]
```
- Build target will be defaulted as DEV if the option is omitted.
- `writeVersionFile` task will generate a file named `version.properties`
  on project root directory.
- Standalone executable JAR file will be created in `build/outputs`
  directory after `deploy` task is finished.

## Technical stacks/libs used:
 - Kotlin 1.1
 - Spring boot 2
   * Externalised configurations
   * I18n support
 - Firebase Cloud Messaging client

## TO-DOs
- Firebase push config
- Undertow
- Error handlers
- JPA
- Make HTTPS as default (letsencrypt + pkcs12 key)
- Unit test with JUnit5 Test code
- Integration test
- Static analysis
- Coverage report
- FCMClient externalisation