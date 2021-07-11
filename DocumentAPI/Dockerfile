FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 9090
VOLUME /tmp
ADD target/DocumentAPI-0.0.1-SNAPSHOT DocumentAPI-0.0.1-SNAPSHOT
ENTRYPOINT ["java","-jar","/DocumentAPI-0.0.1-SNAPSHOT"]
