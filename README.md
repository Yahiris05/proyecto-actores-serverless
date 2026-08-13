# Microservicio Serverless con Modelo de Actores

**Asignatura:** Actividad Semana 14 - Modelo de Actores y Arquitecturas Serverless

##  Descripción del Proyecto
Este proyecto implementa un microservicio concurrente y tolerante a fallos utilizando el **Modelo de Actores** (mediante el framework Akka en Java) desplegado sobre una **Arquitectura Serverless** (AWS Lambda). 

El sistema demuestra cómo encapsular estado y manejar errores (reinicios automáticos sin caída del servidor) dentro del ciclo de vida de una función Lambda, superando las limitaciones tradicionales de las funciones sin estado puro.

## Arquitectura de Componentes
1. **WorkerActor:** Actor encargado de procesar la tarea (convertir texto a mayúsculas). Está programado para lanzar una excepción `RuntimeException` si recibe la cadena de texto exacta `"ERROR"`.
2. **SupervisorActor:** Actor padre que gestiona al Worker. Utiliza una estrategia de supervisión `OneForOneStrategy` para interceptar las excepciones del Worker y reiniciarlo automáticamente, aislando el fallo.
3. **LambdaHandler:** Punto de entrada HTTP de AWS Lambda. Inicializa el `ActorSystem` de forma estática para mitigar latencias de arranque en frío (*cold starts*) y utiliza el patrón *Ask* para delegar el trabajo al Supervisor y retornar la respuesta al cliente.

## Tecnologías Utilizadas
* **Lenguaje:** Java 17
* **Framework de Actores:** Akka Classic (2.6.20)
* **Plataforma Serverless:** AWS Lambda
* **Gestor de Dependencias:** Apache Maven (maven-shade-plugin para generar el Fat JAR)

---

##  Instrucciones de Compilación 
Para compilar este proyecto y generar el ejecutable para AWS, necesitas tener instalados Java 17 y Maven.

1. Clona este repositorio.
2. Abre una terminal en la ruta raíz del proyecto (donde se encuentra el archivo `pom.xml`).
3. Ejecuta el siguiente comando para limpiar dependencias previas y empaquetar el código:
   ```bash
   mvn clean package
