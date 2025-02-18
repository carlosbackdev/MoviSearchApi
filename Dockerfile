# Etapa 1: Construcción de la aplicación
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiar todo el proyecto (no solo pom.xml) para evitar errores en dependencias
COPY . .

# Resolver dependencias antes del build
RUN mvn dependency:resolve && mvn clean package -DskipTests

# Etapa 2: Imagen ligera para ejecución
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar solo el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto para Railway (usa la variable PORT)
EXPOSE 8080

# Ejecutar la aplicación asegurando que use el puerto correcto
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=${PORT}"]
