FROM openjdk:8-alpine
RUN addgroup -S spring && adduser -S spring -G spring
VOLUME /tmp
EXPOSE 8081
ARG DEPENDENCY=target
ADD ${DEPENDENCY}/*.jar appbootrest.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/appbootrest.jar"]
