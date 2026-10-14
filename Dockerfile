FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=build /app/target/*.jar app.jar

ENV APP_PORT=3001
ENV APP_ENV=production
ENV CSV_PATH=/app/data/compras.csv

EXPOSE 3001

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]