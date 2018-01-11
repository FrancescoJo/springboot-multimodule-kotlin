# Spring boot multi-module skeleton
Skeleton construct of any REST API microservices

## How to run
System environment values described below are required to run this app:
```
$ export EXAMPLE_APP_PROFILE={dev|alpha|beta|release}
```

Provide overriding configuration if there is/are problem(s) to run this app:
```
--spring.config.location=file:./application.yml
```
Read [Externalised configurations](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) 
documentation of Spring framework for more information. 

## Technical stacks/libs used:
- Java 8
- Spring boot 1.5
    - Externalised configurations
    - I18n support
- Kotlin 1.1
- Guava 23
- Gson 2.8.2

- \[TBD\] JUnit5 / Spek
- Mockito 1.9

## TO-DOs
- Database correlation
- Error handlers
- Test code
- Java9 migration
- Spring boot 2 migration
- Make HTTPS as default
