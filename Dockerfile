FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY . /app
COPY target/*.jar store-management.jar
ENTRYPOINT ["java","-jar","store-management.jar"]