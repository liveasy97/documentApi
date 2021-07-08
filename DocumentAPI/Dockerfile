FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 9090
VOLUME /tmp
ADD DocumentAPI.jar document.jar
ENTRYPOINT ["java","-jar","/document.jar"]