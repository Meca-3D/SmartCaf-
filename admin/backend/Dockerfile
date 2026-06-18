# ── Stage 1 : Build avec Maven ──────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copier uniquement le pom.xml en premier pour profiter du cache des dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier les sources et compiler
COPY src ./src
RUN mvn package -DskipTests -B

# ── Stage 2 : Image d'exécution légère ──────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# curl pour les healthchecks éventuels
RUN apk add --no-cache curl

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
