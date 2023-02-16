FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*[^p][^l][^a][^i][^n].war
COPY ${JAR_FILE} app.war
ENTRYPOINT ["java","-jar","/app.war"]
