FROM maven:3.8.3-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src ./src
RUN ls /app
RUN mvn package -DskipTests

FROM openjdk:17-slim AS runtime
WORKDIR /app
COPY --from=build /app/target/BookStore-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]