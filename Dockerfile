FROM openjdk:17
LABEL authors="BrainDias"

COPY /build/libs/*.jar ./

ENTRYPOINT ["java","-jar","*.jar"]