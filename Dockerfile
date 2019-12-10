FROM openjdk:11-jdk-slim
ENV PORT 8081
EXPOSE 8081

COPY target/*.jar app.jar
CMD ["java", "-XX:+UseG1GC", "-jar", "app.jar"]