FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y

FROM gradle:8.8-jdk21 AS builder

WORKDIR /app
COPY . .

RUN gradle clean build -x test --info

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/OnlineStore-0.0.1-SNAPSHOT.jar OnlineStore.jar

EXPOSE 8080

CMD ["java","-jar","OnlineStore.jar"]

