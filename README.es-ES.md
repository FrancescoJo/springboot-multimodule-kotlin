# Demostración de multimódulos de Spring boot + Kotlin
Una construcción simple de REST API aplicación basada en la Spring boot. Esta proyecto incluye
un caso de uso simple para referencia de implementación.

Aunque la dificultad de configuración se ha reducido después del lanzamiento de Spring boot,
la configuración de la marco de software todavía no es una tarea fácil. Este proyecto
de demostración es para reducir tal costos en la etapa de desarrollo inicial.

## Utilizadas las pilas/libs técnicas:
  - Build tools:
    * [Gradle](https://gradle.org/) 4.x
  - Idiomas:
    * Java 1.8 - para ejecutar está proyecto
    * [Kotlin](https://kotlinlang.org/) 1.2 - para implementaciónes, prueba de unidad
    * [Groovy](http://groovy-lang.org/) 2.4 - para prueba de integración
  - Marcos de softwares y motores de ejecuciónes:
    * [Undertow](http://undertow.io/) - para Web application server
    * [Spring boot](http://spring.io/projects/spring-boot) 2 - para marcos de softwares
    * JPA con [Hibernate](http://hibernate.org/) - datos persistentes
    * [HicariCP](https://github.com/brettwooldridge/HikariCP) - grupo de conexiones de la bases de datos
    * [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/) - Servicio de envío de mensajes
  - Calidad del código:
    * [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
    * [Spock framework](http://spockframework.org/) con [Groovy](http://groovy-lang.org/)
    * [detekt](https://arturbosch.github.io/detekt/index.html)

## Cómo construir
```
./gradlew clean deploy [writeVersionFile] [-PbuildConfig={DEV|ALPHA|BETA|RELEASE}]
```
- El `buildConfig` será predeterminado como `DEV` cuando se omite la opción.
- `writeVersionFile` ponerá un archivo llamado `version.properties` en el directorio raíz del proyecto.
- El archivo JAR ejecutable independiente se creará en el directorio `build/outputs` después
  `deploy` haya finalizado.

## Cómo ejecutar
```
java -jar `ls build/outputs/*.jar`
```
- O simplemente haga clic doble en el archivo JAR en `build/outputs` en su entorno de escritorio.

## Invocando `Hello, world`
GET solicitud y respuesta
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

POST solicitud y respuesta
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

## Manejo de errores
Todos los problemas lógicos se dirigen a `com.github.fj.restapi.endpoint.CustomErrorHandler` en primero,
y convertido como `com.github.fj.restapi.dto.ErrorResponse` para manejar la respuesta *consistentamente*.
La respuesta de error se verá como:

```
{
  "body": {
    "message": "Not Found",
    "reason": "GeneralHttpException"
  },
  "type": "ERROR"
}
```

Sin este `@ControllerAdvice`, todas las excepciones se manejarán en
`org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController#error` en predeterminado,
que produce el error como:

```
{
  "timestamp": "2018-08-23T02:15:48.527+0000",
  "status": 404,
  "error": "Not Found",
  "message": "Not Found",
  "path": "/"
}
```

se ve bastante diferente a nuestras OK/ERROR respuestas.

## Cómo configurar un SSL
Este proyecto está preparado a ejecutarse como servidor HTTPS independiente con certificado
de Let's encrypt. Sin embargo, porque la naturaleza de sistema Java Keystore,
[se requiere reiniciar el servidor] (https://github.com/spring-projects/spring-boot/issues/5450)
después de que el certificado ha expirado. Para evitar el problema, es mejor
configurar un proxy inverso con demonios HTTP conocidos como `nginx` o `httpd`.

Establecer el modo de operación como SSL es muy simple. Solo crea una clave jks con el comando
[`keytool`] (https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html) o importar
un certificado de CA y convertirlo por el comando [`openssl`] (https://www.openssl.org/docs/man1.0.2/apps/openssl.html),
y cambie su `application.yml` como siguiente:

```
# Setting 'true' will accept HTTPS requests only
security:
  require-ssl: true

server:
  ssl:
    enabled: true
```

Hay algunas scrituras de demostraciónes en el directorio `settings / letsencrypt`. Personalizarlas según sus necesidades.

## Cómo ejecutar pruebas y crear reportes
```
./gradlew test integrationTest jacocoTestReport
```
- Contrar a la tarea `test`, `integrationTest` es se debe ejecutar en un entorno separado.
  Por favor, compruebe los archivos en el directorio `_application/src/integrationTest/resources` para ver un ejemplo.

## Cómo ejecutar análisis estáticos y crear reportes
```
./gradlew detekt
```
- Este proyecto utiliza [detekt] (https://arturbosch.github.io/detekt/index.html) como un analizador estático.
  Lea el documento oficial y modifique el archivo `gradle/scripts/static-analysis-detekt.gradle`, para sus propias necesarias.
