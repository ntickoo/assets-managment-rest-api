FROM openjdk:8-jdk-alpine
RUN addgroup -S assetapp && adduser -S assetapp -G assetapp
USER assetapp:assetapp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]