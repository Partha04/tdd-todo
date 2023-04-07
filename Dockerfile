FROM maven:3.8.6-openjdk-18 as maven
WORKDIR /app
COPY ./pom.xml ./pom.xml
VOLUME /root/.m2
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package -DskipTests && cp ./target/*.jar app.jar

FROM openjdk:18-jdk-alpine3.14
COPY --from=maven /root/.m2 /root/.m2
WORKDIR /app
COPY --from=maven /app/app.jar ./app.jar
CMD ["java", "-jar", "/app/app.jar"]