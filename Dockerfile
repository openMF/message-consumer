FROM azul/zulu-openjdk-alpine:20
COPY build/libs/consumer-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]