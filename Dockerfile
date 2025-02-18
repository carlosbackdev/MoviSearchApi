# Etapa 1: Construcción de la aplicación
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copiar solo el archivo de configuración de Maven para optimizar caché
COPY pom.xml ./

# Crear y usar una carpeta de caché para dependencias
RUN mvn dependency:resolve dependency:go-offline

# Copiar el resto del código fuente
COPY . ./

# Compilar la aplicación sin volver a descargar dependencias
RUN mvn clean package -DskipTests -X

# Etapa 2: Imagen ligera para ejecución
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Configurar el puerto correctamente para Railway
EXPOSE 8080

# Asegurar que se use el puerto correcto
ENTRYPOINT ["sh", "-c", "java -Xmx2048m -jar /app/app.jar --server.port=${PORT:-8080}"]