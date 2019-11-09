FROM openjdk:8-jdk-slim
ENV PORT 8081
EXPOSE 8081

COPY *.jar app.jar
CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-jar", "app.jar"]