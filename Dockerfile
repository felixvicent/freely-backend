#
# Build stage
#
FROM maven:3.8.3-amazoncorretto-17 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/backend-0.0.1-SNAPSHOT.jar backend.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-jar","backend.jar"]