# Spring boot + Kotlin 멀티모듈 데모
Spring boot 기반의 REST API 앱을 만들기 위한 데모 프로젝트입니다. 구현 참고를 위해 간단한 hello world 구현을 포함하고 있습니다.

Spring boot 출시 이후 설정이 많이 쉬워졌다곤 하지만 Spring framework 은 여전히 설정하기 복잡한 프로젝트임에 틀림없습니다.
이 템플릿 프로젝트는 그런 초기 설정에 들이는 시간을 절약하기 위해 만들었습니다.

## 구성에 사용한 라이브러리 및 프레임워크:
  - 빌드 도구:
    * [Gradle](https://gradle.org/) 4.x
  - 언어:
    * Java 1.8 - 프로그램 실행 플랫폼
    * [Kotlin](https://kotlinlang.org/) 1.2 - 구현 및 단위 테스트에 사용
    * [Groovy](http://groovy-lang.org/) 2.5 - 통합 테스트에 사용
  - 프레임워크 및 앱 런타임:
    * [Undertow](http://undertow.io/) - Tomcat/Jetty 등을 대신하는 서블릿 컨테이너
    * [Spring boot](http://spring.io/projects/spring-boot) 2 - 애플리케이션 프레임워크
    * [Hibernate](http://hibernate.org/) 및 JPA - 데이터베이스
    * [HicariCP](https://github.com/brettwooldridge/HikariCP) - 데이터베이스 connection pool 관리
    * [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/) - 모바일 및 웹 클라이언트 푸시 전송 서비스
  - 코드 품질 관리:
    * [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
    * [Spock framework](http://spockframework.org/) with [Groovy](http://groovy-lang.org/)
    * [detekt](https://arturbosch.github.io/detekt/index.html)

## 빌드하는 법
```
./gradlew clean deploy [writeVersionFile] [-PbuildConfig={DEV|ALPHA|BETA|RELEASE}]
```
- `buildConfig` 를 지정하지 않으면, `DEV` 가 자동 선택됩니다.
- `writeVersionFile` 는 프로젝트 루트 디렉토리에 `version.properties` 이란 이름의 파일을 생성합니다.
- `deploy` 를 실행하면 단독 실행가능한 JAR 파일이 `build/outputs` 에 생성됩니다.

## 실행하는 법
```
java -jar `ls build/outputs/*.jar`
```
- 혹은 JAR 파일을 바로 실행할 수 있는 데스크탑이라면 `build/outputs` 의 JAR 파일을 더블클릭 하시면 됩니다.

## `Hello, world` 예제 호출하기
GET 요청 및 응답
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

POST 요청 및 응답
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

## 오류 처리
모든 프로그램 오류는 `com.github.fj.restapi.endpoint.CustomErrorHandler` 를 최초로 거친 뒤,
*일관성 있는* 오류 처리를 위해 `com.github.fj.restapi.dto.ErrorResponse` 형태로 가공됩니다.
클라이언트에게 전달되는 오류의 형태는 다음과 같습니다.

```
{
  "body": {
    "message": "Not Found",
    "reason": "GeneralHttpException"
  },
  "type": "ERROR"
}
```

이 `@ControllerAdvice` 구현을 사용하지 않으면 모든 프로그램 오류는 Spring boot 의
`org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController#error` 를 거치게 됩니다.
이 로직을 거친 오류의 형태는 다음과 같습니다.

```
{
  "timestamp": "2018-08-23T02:15:48.527+0000",
  "status": 404,
  "error": "Not Found",
  "message": "Not Found",
  "path": "/"
}
```

이는 우리가 지정한 OK 혹은 ERROR 의 형태와 다르기 때문에 클라이언트측의 구현 비용이 추가로 발생합니다.

## SSL 설정하기
이 프로젝트는 Let's encrypt 서비스로부터 제공받은 SSL 인증서를 설정해 단독 HTTPS 서버로
운영 가능하도록 구성했습니다. 하지만 Java Keystore 시스템의 특성으로 인해 인증서 만료시
[서버 재시작이 필요](https://github.com/spring-projects/spring-boot/issues/5450)합니다.
이 문제를 해결하려면 `nginx` 나 `httpd` 같은 잘 알려진 웹 서버를 사용해 어플리케이션으로 요청을
역방향으로 전달(reversy proxy) 하도록 구성해야 합니다. 또한 역방향 프록시 구성의 잇점은
그런 서비스들의 서버 재시작 속도가 스프링 애플리케이션의 시작 속도보다 훨씬 빠르다는 것입니다.
따라서 역방향 프록시를 담당하는 쪽에만 SSL을 잘 설정해 둔다면 서버 순단 비용을 줄일 수 있습니다.

하지만 인증서가 준비되지 않은 상황에서는 SSL 을 활성화할 경우 서버를 시작할 수 없습니다. 물론
SSL 모드로 앱을 동작하게 하는 방법은 매우 간단합니다. [`keytool`](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html) 명령으로
jks(Java KeyStore) 포맷의 키스토어를 하나 만들거나 혹은 인증기관으로부터 받은 CA 인증서를 [`openssl`](https://www.openssl.org/docs/man1.0.2/apps/openssl.html) 명령을 사용해
`pkcs12` 형식으로 변환한 뒤 `application.yml` 의 ssl 옵션을 아래 예제와 같이 활성화 하면 됩니다.

```
# Setting 'true' will accept HTTPS requests only
security:
  require-ssl: true

server:
  ssl:
    enabled: true
```

자세한 내용은 `settings/letsencrypt` 디렉토리 내의 예제 스크립트의 내용을 참조하시기 바랍니다.

## 테스트 실행 및 테스트 커버리지 리포트 보기
```
./gradlew test integrationTest jacocoTestReport
```
- `test` 작업과는 달리 `integrationTest` 는 운영 환경과 분리된 별도 환경을 필요로 합니다. 운영중인 서버 혹은
  데이터베이스에서 integrationTest 를 실행할 경우 자칫 운영 데이터베이스에 피해를 입힐 수 있으니 주의해야 합니다.
  `_application/src/integrationTest/resources` 디렉토리의 파일을 확인해주세요.

## 프로그램 정적 분석 및 리포트 보기
```
./gradlew detekt
```
- 이 프로젝트는 [detekt](https://arturbosch.github.io/detekt/index.html) 를 정적 분석 도구로 활용합니다.
  예제 설정이 마음에 들지 않으신다면 `gradle/scripts/static-analysis-detekt.gradle` 파일을 공식 문서의 가이드대로 입맛에 맞게 수정하시기 바랍니다.
