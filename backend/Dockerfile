FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn package -DskipTests -q

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
RUN addgroup -S arcane && adduser -S arcane -G arcane
COPY --from=build /app/target/*.jar app.jar
USER arcane
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
