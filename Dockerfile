FROM openjdk:8-jre
ADD target/bookstore-0.0.1-SNAPSHOT.jar app.jar
RUN /bin/sh -c /bin/sh -c make /sec
EXPOSE 8084
ENTRYPOINT ["java","-jar","/app.jar"] 