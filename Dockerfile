FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

FROM gradle:8.8-jdk17 AS builder

WORKDIR /app
COPY . .

RUN gradle clean build -x test --info

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/Urban-market-0.0.1-SNAPSHOT.jar Urban-market.jar

EXPOSE 8080

CMD ["java","-jar","Urban-market.jar"]