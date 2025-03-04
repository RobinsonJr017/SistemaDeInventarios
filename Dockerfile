# Usa una imagen de Java con JDK 17
FROM openjdk:17-jdk-slim

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR generado dentro del contenedor
COPY target/*.jar app.jar

# Expone el puerto 8080 (donde corre Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
