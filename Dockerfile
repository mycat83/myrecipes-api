FROM openjdk:11-jdk-slim

COPY target/*.jar app.jar
CMD ["java", "-XX:+UseG1GC", "-XX:MaxMetaspaceSize=512m", "-XX:MetaspaceSize=256m", "-jar", "app.jar"]