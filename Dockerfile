FROM gradle:jdk17 AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle bootJar -x test

# actual container
FROM eclipse-temurin:17-jdk-alpine
ENV ARTIFACT_NAME=order-quantity-analyzer-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "order-quantity-analyzer-0.0.1-SNAPSHOT.jar"]
