FROM openjdk:8-alpine

COPY target/uberjar/ry-middle-front.jar /ry-middle-front/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/ry-middle-front/app.jar"]
