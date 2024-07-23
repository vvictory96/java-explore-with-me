FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar main-app.jar
ENTRYPOINT ["java","-jar","/main-app.jar"]