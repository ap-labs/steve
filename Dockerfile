## build stage
#FROM maven:3.6.1-jdk-11
#
#ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
#
#ARG DB_HOST
#ARG DB_USERNAME
#ARG DB_PASSWORD
#ARG DB_DATABASE
#ARG DB_PORT
#
#WORKDIR /code
#
#ADD /src /code/src
#ADD /website /code/website
#ADD /pom.xml /code/pom.xml
#
#RUN mvn clean package -Pdocker -Djdk.tls.client.protocols="TLSv1,TLSv1.1,TLSv1.2"

# run stage
FROM openjdk:11-slim-buster

RUN apt-get update && apt-get install -y libwebp6 libwebp-dev curl fontconfig ttf-dejavu && apt-get clean

WORKDIR /code

ADD /src /code/src
ADD /website /code/website
ADD /pom.xml /code/pom.xml
COPY /target/libs /code/libs
COPY /target/steve.jar /code/app.jar
COPY /docker-entrypoint.sh /code/

ENTRYPOINT ["./docker-entrypoint.sh"]
