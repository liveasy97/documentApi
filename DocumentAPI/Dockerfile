FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 9090
VOLUME /tmp
ADD DocumentAPI-0.0.1-SNAPSHOT.jar DocumentAPI-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/DocumentAPI-0.0.1-SNAPSHOT.jar"]