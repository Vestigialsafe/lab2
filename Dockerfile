# Etapa 1: construcción con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiar el proyecto completo (incluyendo subcarpeta laboratorio-master)
COPY laboratorio-master ./laboratorio-master

# Ir al subdirectorio que contiene el pom.xml y construir el jar
WORKDIR /app/laboratorio-master
RUN mvn clean package -DskipTests

# Etapa 2: imagen ligera solo con el JAR
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app

# Copiar el jar compilado desde la etapa anterior
COPY --from=builder /app/laboratorio-master/target/*.jar app.jar

# Exponer el puerto configurado en application.properties
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
