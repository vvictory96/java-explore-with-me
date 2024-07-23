FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats-app.jar
ENTRYPOINT ["java","-jar","/stats-app.jar"]