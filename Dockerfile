FROM openjdk:17
LABEL authors="BrainDias"

COPY /build/libs/demo-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java","-jar","/demo-0.0.1-SNAPSHOT.jar"]