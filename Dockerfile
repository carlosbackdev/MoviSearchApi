# Etapa 1: Construcción de la aplicación
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo pom.xml y descargamos las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente del proyecto
COPY src /app/src

# Construimos el proyecto con Maven
RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera para ejecución
FROM eclipse-temurin:17-jdk-alpine

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]